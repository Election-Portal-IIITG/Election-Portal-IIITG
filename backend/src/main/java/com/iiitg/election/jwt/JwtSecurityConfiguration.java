package com.iiitg.election.jwt;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
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
//		http.oauth2ResourceServer((oauth2)->oauth2.jwt(withDefaults()));
		
		// Building and returning the HttpSecurity configuration
		return http.build();
		
		
	}
}
