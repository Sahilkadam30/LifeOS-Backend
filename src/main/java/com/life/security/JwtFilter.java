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

        // ✅ Allow WebSocket & SockJS info
        if (request.getRequestURI().startsWith("/ws") || request.getRequestURI().startsWith("/api/ws") || request.getRequestURI().startsWith("/api/info")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Allow preflight
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7).trim();
        } else if (request.getParameter("token") != null) {
            token = request.getParameter("token").trim();
            if (token.startsWith("Bearer ")) {
                token = token.substring(7).trim();
            }
        }

        if (token != null) {
            token = token.trim();
            if (token.startsWith("\"") && token.endsWith("\"") && token.length() > 2) {
                token = token.substring(1, token.length() - 1).trim();
            }
            if (token.isEmpty() || "null".equalsIgnoreCase(token) || "undefined".equalsIgnoreCase(token)) {
                token = null;
            }
        }

        try {
            if (token != null) {
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
            String uri = request.getRequestURI();
            if (uri != null && (uri.contains("/stream") || uri.contains("/download") || uri.startsWith("/api/music/"))) {
                filterChain.doFilter(request, response);
                return;
            }

            // 🔥 THIS IS THE KEY FIX
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired");
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }
}