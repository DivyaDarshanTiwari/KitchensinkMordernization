package org.mongodb.springboot.kitchensinkmordernization.security;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.mongodb.springboot.kitchensinkmordernization.repositories.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final MemberRepository memberRepository;
    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  memberRepository.findMemberByName(username);
    }
}
