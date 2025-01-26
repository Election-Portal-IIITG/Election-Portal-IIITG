package com.iiitg.election.jwt;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class JwtAuthenticationResource {
	
	private JwtEncoder jwtEncoder;
	
	public JwtAuthenticationResource(JwtEncoder jwtEncoder) {
		super();
		this.jwtEncoder = jwtEncoder;
	}



	@PostMapping("/authenticate")
	public JwtResponse authenticate(Authentication authentication) {
		System.out.println("Hello");
		System.out.println("Authentication" + authentication);
		return new JwtResponse(createToken(authentication));
	}
	
//	@PostMapping("/authenticate")
//	public JwtResponse authenticate(@RequestBody LoginRequest loginRequest) {
//		System.out.println("loginrequest" + loginRequest);
//	    // Manually create authentication
//	    Authentication authentication = authenticationManager.authenticate(
//	        new UsernamePasswordAuthenticationToken(
//	            loginRequest.username(), 
//	            loginRequest.password()
//	        )
//	    );
//	    return new JwtResponse(createToken(authentication));
//	}

	// Add this record
	record LoginRequest(String username, String password) {}



	private String createToken(Authentication authentication) {
		var claims = JwtClaimsSet.builder()
			.issuer("self")
			.issuedAt(Instant.now())
			.expiresAt(Instant.now().plusSeconds(60 * 60))
			.subject(authentication.getName())
			.claim("scope", createScope(authentication))
			.build();
		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
	
	private String createScope(Authentication authentication) {
		return authentication.getAuthorities().stream()
			.map(a -> a.getAuthority())
			.collect(Collectors.joining(" "));
	}
}


record JwtResponse(String token) {}