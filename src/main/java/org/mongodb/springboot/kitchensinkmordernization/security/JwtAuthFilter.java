package org.mongodb.springboot.kitchensinkmordernization.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.mongodb.springboot.kitchensinkmordernization.repositories.MemberRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final AuthUtil authUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("Request URI : {}", request.getRequestURI());
            String authenticationHeader = request.getHeader("Authorization");
            if (authenticationHeader != null && !authenticationHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String authenticationToken = authenticationHeader != null ? authenticationHeader.split("Bearer ")[1] : null;
            String username = authUtil.getUserNameFromToken(authenticationToken);
            if (authenticationToken != null && SecurityContextHolder.getContext()
                    .getAuthentication() == null) {
                UserDetails userDetails = memberRepository.findMemberByName(username);
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext()
                        .setAuthentication(token);
            }

            filterChain.doFilter(request, response);
    }
}
