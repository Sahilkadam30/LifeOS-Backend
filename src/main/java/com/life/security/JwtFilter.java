package com.life.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.life.service.CustomUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException; // ✅ ADD
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

        // ✅ Allow WebSocket
        if (request.getRequestURI().startsWith("/ws")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Allow preflight
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                String token = authHeader.substring(7);

                String username = jwtUtil.extractUsername(token);

                if (username != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                	UserDetails userDetails = service.loadUserByUsername(username);

                	if (jwtUtil.validateToken(token, userDetails)) {

                	    UsernamePasswordAuthenticationToken authToken =
                	            new UsernamePasswordAuthenticationToken(
                	                    userDetails,
                	                    null,
                	                    userDetails.getAuthorities()
                	            );

                	    authToken.setDetails(
                	            new WebAuthenticationDetailsSource().buildDetails(request)
                	    );

                	    SecurityContextHolder.getContext().setAuthentication(authToken);
                	}
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {

            // 🔥 THIS IS THE KEY FIX
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired");
        }
    }
}