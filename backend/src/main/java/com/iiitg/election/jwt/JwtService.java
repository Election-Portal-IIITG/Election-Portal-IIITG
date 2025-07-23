package com.iiitg.election.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${jwt.secret}")
	private String secretKey;

	public String generateToken(String username) {
		
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
				.and()
				.signWith(getKey())
				.compact();
	}
	
    // NEW METHODS FOR NOMINATION/APPROVAL TOKENS
    
    /**
     * Generate token for nomination requests (24 hour expiry)
     */
    public String generateNominationToken(String candidateEmailId, String nominatorEmailId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "NOMINATION");
        claims.put("candidateEmailId", candidateEmailId);
        claims.put("nominatorEmailId", nominatorEmailId);
        
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject("NOMINATION_REQUEST")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24 hours
                .and()
                .signWith(getKey())
                .compact();
    }
    
    /**
     * Generate token for approval requests (24 hour expiry)
     */
    public String generateApprovalToken(String candidateEmailId, String facultyEmailId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "APPROVAL");
        claims.put("candidateEmailId", candidateEmailId);
        claims.put("facultyEmailId", facultyEmailId);
        
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject("APPROVAL_REQUEST")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24 hours
                .and()
                .signWith(getKey())
                .compact();
    }
    
    /**
     * Validate and extract nomination/approval token data
     */
    public TokenData validateActionToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            
            // Check if token is expired
            if (isTokenExpired(token)) {
                throw new RuntimeException("Token has expired");
            }
            
            String type = claims.get("type", String.class);
            String candidateEmailId = claims.get("candidateEmailId", String.class);
            
            if ("NOMINATION".equals(type)) {
                String studentEmailId = claims.get("studentEmailId", String.class);
                return new TokenData(type, candidateEmailId, studentEmailId, null);
            }
            else if ("APPROVAL".equals(type)) {
            	String facultyEmailId = claims.get("facultyEmailId", String.class);
            	return new TokenData(type, candidateEmailId, null, facultyEmailId);
            }
            
            throw new RuntimeException("Invalid token type");
            
        } catch (Exception e) {
            throw new RuntimeException("Invalid token: " + e.getMessage());
        }
    }


	private Key getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractUserName(String token) {
		// TODO Auto-generated method stub
		return extractClaim(token, Claims::getSubject);
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


	public boolean validate(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
