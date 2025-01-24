package com.iiitg.election.jwt;

import static org.springframework.security.config.Customizer.withDefaults;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

//@Configuration
public class JwtSecurityConfiguration {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		// Authorizing all requests - any incoming request must be authenticated
		http.authorizeHttpRequests(requests -> requests.anyRequest().authenticated());

		// Disabling session - enforcing state-less session management
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Enabling basic HTTP authentication for the requests
		http.httpBasic(withDefaults());
		
		// Disabling CSRF - not needed for state-less REST APIs
		http.csrf(csrf -> csrf.disable());
		
		//Disabling frame options - allows embedding the application in i frames
		//h2 console uses frames but by default spring does not allows it
		http.headers(header->header.frameOptions(frameOptions -> frameOptions.disable()));
		
	    // Configuring the application as an OAuth2 Resource Server
	    // This enables JWT-based authentication for incoming requests (will add later)
		http.oauth2ResourceServer((oauth2)->oauth2.jwt(withDefaults()));
//		http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
		
		// Building and returning the HttpSecurity configuration
		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailService() {
		var user = User.withUsername("user")
				.password("{noop}dummy")
//				.password("dummy")
//				.passwordEncoder(str -> passwordEncoder().encode(str))
				.roles("USER")
				.build();
		var admin = User.withUsername("admin")
				.password("{noop}dummy")
//				.password("dummy")
//				.passwordEncoder(str -> passwordEncoder().encode(str))
				.roles("ADMIN")
				.build();
		
		return new InMemoryUserDetailsManager(user, admin);
		
	}
	
	@Bean
	public KeyPair keyPair() {
		try {
			var keyPairGenerator = KeyPairGenerator.getInstance("RSA");	
			keyPairGenerator.initialize(2048);
			return keyPairGenerator.generateKeyPair();
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Bean
	public RSAKey rsaKey(KeyPair keyPair) {
		return new RSAKey.Builder((RSAPublicKey)keyPair.getPublic())
				.privateKey(keyPair.getPrivate())
				.keyID(UUID.randomUUID().toString())
				.build();
	}
	
	@Bean
	public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
		var jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, context) -> jwkSelector.select(jwkSet);
	}
	
	@Bean
	public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
		return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
	}
	
	@Bean
	public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}
	
	
}
