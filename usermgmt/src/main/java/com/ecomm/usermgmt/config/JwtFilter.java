package com.ecomm.usermgmt.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecomm.usermgmt.service.JwtService;
import com.ecomm.usermgmt.service.RedisService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	ApplicationContext context;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain chain)
	        throws ServletException, IOException {

	    String authHeader = request.getHeader("Authorization");

	    if (authHeader != null
	            && authHeader.startsWith("Bearer ")
	            && SecurityContextHolder.getContext().getAuthentication() == null) {

	        String token = authHeader.substring(7);

	        try {
	            // 1) Validate token (signature/exp); no DB hit
	            if (jwtService.jwtValidateToken(token)) {

	                String username = jwtService.extractUserName(token);
	                String role     = jwtService.extractUserRole(token);

	                // Guard: if role missing, deny
	                if (role == null || role.isBlank()) {
	                    SecurityContextHolder.clearContext();
	                    chain.doFilter(request, response);
	                    return;
	                }

	                // 3) Map role -> authority
	                var authority   = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
	                var authorities = java.util.List.of(authority);

	                // 4) Build principal straight from claims
	                UserDetails principal = org.springframework.security.core.userdetails.User
	                        .withUsername(username)
	                        .password("N/A")
	                        .authorities(authorities)
	                        .accountExpired(false).accountLocked(false)
	                        .credentialsExpired(false).disabled(false)
	                        .build();

	                var authToken = new UsernamePasswordAuthenticationToken(principal, null, authorities);
	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authToken);

	                redisService.setKey(username, role.toLowerCase());
	                
	            }
	        } catch (io.jsonwebtoken.JwtException e) {
	            SecurityContextHolder.clearContext();
	        }
	    }

	    chain.doFilter(request, response);
	}


	
}
