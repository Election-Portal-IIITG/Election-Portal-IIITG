package com.iiitg.election.jwt.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iiitg.election.jwt.JwtService;
import com.iiitg.election.jwt.service.CustomUserDetailsService;
import com.iiitg.election.payload.response.ErrorResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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
     * Filters incoming HTTP requests to validate JWT tokens and set up the security context.
     *
     * <p>This method is part of the Spring Security filter chain and is responsible for
     * extracting and validating JWT tokens from the "Authorization" header. If the token
     * is valid, it sets up the security context with the authenticated user's details.
     *
     * <p><strong>Error Handling:</strong>
     * The method handles exceptions related to JWT tokens, such as expired or malformed tokens,
     * by sending appropriate error responses to the client.
     *
     * @param request     the HttpServletRequest object representing the incoming request.
     *                    Must not be null.
     * @param response    the HttpServletResponse object representing the outgoing response.
     *                    Must not be null.
     * @param filterChain the FilterChain object to continue the processing of the request.
     *                    Must not be null.
     *
     * @throws ServletException if there is an error in the servlet processing.
     * @throws IOException      if an input or output exception occurs.
     *
     * @author Animesh Kumar (Initial implementation)
     * @author Ishaan Das (Added error handling for JWT exceptions)
     */
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
	                                @NonNull HttpServletResponse response,
	                                @NonNull FilterChain filterChain)
	                                throws ServletException, IOException {
	    String authHeader = request.getHeader("Authorization");
	    String token = null;
	    String username = null;

	    try {
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            token = authHeader.substring(7);
	            username = jwtService.extractUserName(token);
	        }

	        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	            UserDetails userDetails = context.getBean(CustomUserDetailsService.class).loadUserByUsername(username);

	            if (jwtService.validate(token, userDetails)) {
	                UsernamePasswordAuthenticationToken authToken =
	                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authToken);
	            }
	        }

	        filterChain.doFilter(request, response);

	    } catch (ExpiredJwtException ex) {
	        sendErrorResponse(response, "JWT token expired", HttpStatus.UNAUTHORIZED);
	    } catch (JwtException ex) {
	        sendErrorResponse(response, "Malformed or invalid JWT token", HttpStatus.UNAUTHORIZED);
	    }
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
