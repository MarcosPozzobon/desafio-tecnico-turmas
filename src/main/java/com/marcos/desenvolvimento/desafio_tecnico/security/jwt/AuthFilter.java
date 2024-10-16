package com.marcos.desenvolvimento.desafio_tecnico.security.jwt;

import com.marcos.desenvolvimento.desafio_tecnico.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthFilter(TokenUtils tokenUtils, UserDetailsServiceImpl userDetailsServiceImpl){
        this.tokenUtils = tokenUtils;
        this.userDetailsService = userDetailsServiceImpl;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            var token = tokenUtils.getTokenFromRequest(request);

            if (token != null && tokenUtils.validateToken(token)) {
                var username = tokenUtils.getUserName(token);
                var loadedUser = userDetailsService.loadUserByUsername(username);

                if (!loadedUser.isEnabled()) {
                    throw new AccessDeniedException("Usuário desativado!");
                }

                var authentication = new UsernamePasswordAuthenticationToken(loadedUser, null, loadedUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            LOGGER.error("Não foi possível setar a autenticação: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
