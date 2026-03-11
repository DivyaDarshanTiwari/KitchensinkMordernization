package org.mongodb.springboot.kitchensinkmordernization.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.mongodb.springboot.kitchensinkmordernization.repositories.MemberRepository;
import org.mongodb.springboot.kitchensinkmordernization.services.AuthService;
import org.mongodb.springboot.kitchensinkmordernization.services.implementation.TokenBlacklistServiceImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Collection;
import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthUtil authUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final TokenBlacklistServiceImpl tokenBlacklistService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain )  {
        try {
            log.info("Request URI : {}", request.getRequestURI());
            String authenticationHeader = request.getHeader("Authorization");
            if(authenticationHeader == null){
                filterChain.doFilter(request,response);
                return;
            }
            if (!authenticationHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String authenticationToken = authenticationHeader.split("Bearer ")[1];
            if (authenticationToken != null && tokenBlacklistService.isBlacklisted(authenticationToken)) {
                throw new JwtException("Token has been invalidated. Please log in again.");
            }
            Collection<? extends GrantedAuthority> roles = authUtil.getAuthoritiesFromToken(authenticationToken);
            String userId = authUtil.getUserIdFromToken(authenticationToken);
            if (authenticationToken != null && SecurityContextHolder.getContext()
                    .getAuthentication() == null) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userId, null, roles);
                SecurityContextHolder.getContext()
                        .setAuthentication(token);
            }

            filterChain.doFilter(request, response);
        }catch (Exception ex){
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }

}
