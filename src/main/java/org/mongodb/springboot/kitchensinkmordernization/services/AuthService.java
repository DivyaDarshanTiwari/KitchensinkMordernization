package org.mongodb.springboot.kitchensinkmordernization.services;

import org.mongodb.springboot.kitchensinkmordernization.dto.LoginRequestDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.LoginResponseDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    void signUp(MemberDTO memberDTO);
}
