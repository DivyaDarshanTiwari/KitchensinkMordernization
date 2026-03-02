package org.mongodb.springboot.kitchensinkmordernization.security;

import jakarta.servlet.FilterChain;

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
import org.springframework.web.servlet.HandlerExceptionResolver;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final AuthUtil authUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;

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
            String email = authUtil.getEmailFromToken(authenticationToken);
            if (authenticationToken != null && SecurityContextHolder.getContext()
                    .getAuthentication() == null) {
                UserDetails userDetails = memberRepository.findMemberByEmail(email);
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext()
                        .setAuthentication(token);
            }

            filterChain.doFilter(request, response);
        }catch (Exception ex){
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }

}
