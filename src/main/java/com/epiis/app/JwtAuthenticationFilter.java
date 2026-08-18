package com.epiis.app;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.epiis.app.business.UserBusiness;
import com.epiis.app.entity.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserBusiness userBusiness;

    public JwtAuthenticationFilter(JwtService jwtService, UserBusiness userBusiness) {
        this.jwtService = jwtService;
        this.userBusiness = userBusiness;
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/user/login") || path.equals("/user/register");
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        
        String jwt = authHeader.substring(7);
        String userId = jwtService.extractUsername(jwt);

        
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userBusiness.getById(userId);

            
            if (user != null && jwtService.isTokenValid(jwt, userId)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user.getIdUser(), 
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("🟢 JWT válido - Usuario autenticado: " + userId);
            }
        }

        filterChain.doFilter(request, response);
    }
}
