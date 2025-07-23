package com.iiitg.election.jwt.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iiitg.election.jwt.JwtService;
import com.iiitg.election.jwt.service.CustomUserDetailsService;
import com.iiitg.election.payload.response.ErrorResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private ApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Validates JWT tokens from incoming requests and establishes the security context.
     * 
     * <p>Processes the "Authorization" header to:
     * <ul>
     *   <li>Extract and validate JWT tokens
     *   <li>Set up the authenticated user's security context
     *   <li>Handle all JWT-related exceptions with appropriate error responses
     * </ul>
     *
     * <p><b>Error Handling:</b> Specific responses are returned for:
     * <ul>
     *   <li>Missing/invalid Authorization header
     *   <li>Expired tokens
     *   <li>Malformed tokens
     *   <li>Signature issues
     *   <li>User not found
     * </ul>
     *
     * @param request The incoming HTTP request
     * @param response The outgoing HTTP response
     * @param filterChain The filter chain to continue processing
     * @throws ServletException For servlet processing errors
     * @throws IOException For I/O errors

     * @author Animesh Kumar (Initial implementation)
     * @author Ishaan Das (Added comprehensive error handling for JWT exceptions)
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
                                    throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check if authentication is required for this endpoint
        if (requiresAuthentication(request)) {
            try {
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    sendErrorResponse(response, "Authorization header is missing or invalid. Please provide a valid Bearer token.", HttpStatus.UNAUTHORIZED);
                    return;
                }

                token = authHeader.substring(7);
                username = jwtService.extractUserName(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = context.getBean(CustomUserDetailsService.class).loadUserByUsername(username);

                    if (jwtService.validate(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

            } catch (ExpiredJwtException ex) {
                sendErrorResponse(response, "JWT token has expired. Please obtain a new token.", HttpStatus.UNAUTHORIZED);
                return;
            } catch (MalformedJwtException ex) {
                sendErrorResponse(response, "JWT token is malformed. Please provide a valid token.", HttpStatus.UNAUTHORIZED);
                return;
            } catch (SignatureException ex) {
                sendErrorResponse(response, "JWT token signature is invalid. Please provide a valid token.", HttpStatus.UNAUTHORIZED);
                return;
            } catch (UnsupportedJwtException ex) {
                sendErrorResponse(response, "JWT token format is not supported. Please provide a valid token.", HttpStatus.UNAUTHORIZED);
                return;
            } catch (IllegalArgumentException ex) {
                sendErrorResponse(response, "JWT token is empty or contains only whitespace. Please provide a valid token.", HttpStatus.UNAUTHORIZED);
                return;
            } catch (UsernameNotFoundException ex) {
                sendErrorResponse(response, "User not found. Please check your credentials.", HttpStatus.UNAUTHORIZED);
                return;
            } catch (JwtException ex) {
                sendErrorResponse(response, "Invalid JWT token. Please provide a valid token.", HttpStatus.UNAUTHORIZED);
                return;
            } catch (Exception ex) {
                sendErrorResponse(response, "Authentication failed. Please provide a valid token.", HttpStatus.UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
	
    /**
     * Determines whether the requested path requires JWT authentication.
     * 
     * <p>This method checks if the requested path matches any of the predefined public endpoints
     * that are exempt from authentication. Public endpoints typically include login/registration
     * pages and other openly accessible API routes.</p>
     * 
     * @param request The HTTP request object containing the path to check
     * @return {@code false} if the path matches a public endpoint (no auth required),
     *         {@code true} if the path is protected (auth required)
     * 
     * @author Ishaan Das (Added comprehensive error handling for JWT exceptions)
     */
	private boolean requiresAuthentication(HttpServletRequest request) {
	    String path = request.getServletPath();
	    
	    // Set of public endpoints that don't require authentication
	    Set<String> publicEndpoints = new HashSet<>(Arrays.asList(
	    	    "/register-manager",
	    	    "/login-manager",
	    	    "/register-faculty",
	    	    "/login-faculty",
	    	    "/api/students/login",
	            "/api/candidates/nomination-response",  // NEW: Allow nomination responses
	            "/api/candidates/approval-response"     // NEW: Allow approval responses
	    	));
	    
	    // Check if the request path matches any public endpoint
	    return !publicEndpoints.contains(path);
	}

	/**
	 * Sends an error response to the client with the specified HTTP status and message.
	 *
	 * <p>This method constructs an error response in JSON format and sends it to the client.
	 * It sets the appropriate HTTP status code, content type, and character encoding
	 * to ensure the response is correctly interpreted by the client.
	 *
	 * <p><strong>Response Details:</strong>
	 * The response body is a JSON representation of an {@link ErrorResponse} object,
	 * which includes the HTTP status and the error message. The content type is set
	 * to "application/json" and the character encoding is set to "UTF-8".
	 *
	 * <p><strong>Error Handling:</strong>
	 * This method handles IOExceptions that may occur during the writing of the response.
	 * It is the responsibility of the caller to ensure that the response object is valid
	 * and not committed before calling this method.
	 *
	 * @param response the HttpServletResponse object to which the error response will be written.
	 *                 Must not be null and should not be committed.
	 * @param message  the error message to be included in the response. Must not be null.
	 * @param status   the HTTP status code to be set in the response. Must not be null.
	 *
	 * @throws IOException if an input or output exception occurs while writing the response.
	 *
	 * @author Ishaan Das
	 */
    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(status, message);
        String json = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }

}
