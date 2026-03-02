package org.mongodb.springboot.kitchensinkmordernization.services.implementation;

import lombok.RequiredArgsConstructor;
import org.mongodb.springboot.kitchensinkmordernization.config.MapperConfig;
import org.mongodb.springboot.kitchensinkmordernization.dto.LoginRequestDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.LoginResponseDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberDTO;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.mongodb.springboot.kitchensinkmordernization.enums.MemberRole;
import org.mongodb.springboot.kitchensinkmordernization.repositories.MemberRepository;
import org.mongodb.springboot.kitchensinkmordernization.repositories.custom.SequenceGeneratorRepository;
import org.mongodb.springboot.kitchensinkmordernization.security.AuthUtil;
import org.mongodb.springboot.kitchensinkmordernization.services.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final PasswordEncoder passwordEncoder;
    private final MapperConfig mapperConfig;
    private final SequenceGeneratorRepository sequenceGeneratorRepository;
    private final MemberRepository memberRepository;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail().toLowerCase(), loginRequestDto.getPassword()));
        Member member = (Member) authentication.getPrincipal();

        assert member != null;
        String token = authUtil.generateJwtToken(member);
        return new LoginResponseDto(token, member.getId(), member.getRole());
    }

    @Transactional
    public void signUp(MemberDTO memberDTO) {
        Member member = mapperConfig.mapMemberDTOToMember(memberDTO);
        Long sequence = sequenceGeneratorRepository.generateSequenceByName("members");
        member.setId(sequence);
        member.setEmail(memberDTO.getEmail().toLowerCase());
        member.setRole(Set.of(MemberRole.MEMBER));
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
    }


}
