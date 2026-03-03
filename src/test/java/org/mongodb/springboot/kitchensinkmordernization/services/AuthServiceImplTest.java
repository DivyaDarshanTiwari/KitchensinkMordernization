package org.mongodb.springboot.kitchensinkmordernization.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mongodb.springboot.kitchensinkmordernization.config.MapperConfig;
import org.mongodb.springboot.kitchensinkmordernization.dto.LoginRequestDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.LoginResponseDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberDTO;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.mongodb.springboot.kitchensinkmordernization.enums.MemberRole;
import org.mongodb.springboot.kitchensinkmordernization.repositories.MemberRepository;
import org.mongodb.springboot.kitchensinkmordernization.repositories.custom.SequenceGeneratorRepository;
import org.mongodb.springboot.kitchensinkmordernization.security.AuthUtil;
import org.mongodb.springboot.kitchensinkmordernization.services.implementation.AuthServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AuthUtil authUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MapperConfig mapperConfig;
    @Mock
    private SequenceGeneratorRepository sequenceGeneratorRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private MemberDTO memberDTO;
    private Member member;
    private LoginResponseDto loginResponseDTO;
    private LoginRequestDto loginRequestDto;


    @BeforeEach
    void setUp() {
        this.memberDTO = MemberDTO.builder()
                .name("Divya")
                .email("divya@gmail.com")
                .phoneNumber("1111111111")
                .password("Hello123*")
                .build();
        this.member = Member.builder()
                .id(1L)
                .name("Divya")
                .email("divya@gmail.com")
                .phoneNumber("1111111111")
                .password("")
                .role(Set.of(MemberRole.MEMBER))
                .build();

        this.loginResponseDTO = new LoginResponseDto("hello" , 1L , Set.of(MemberRole.MEMBER));
        this.loginRequestDto = new LoginRequestDto("divya@gmail.com","");

    }

    @Nested
    @DisplayName("SignUp TestCases")
    class SignUpTestCases {

        @Test
        @DisplayName("SignUp Successfully with correct Data being sent")
        void testSignUp() {
            when(mapperConfig.mapMemberDTOToMember(memberDTO)).thenReturn(member);
            when(sequenceGeneratorRepository.generateSequenceByName("members")).thenReturn(1L);
            when(passwordEncoder.encode(member.getPassword())).thenReturn("");
            when(memberRepository.save(member)).thenReturn(member);

            //When
            authService.signUp(memberDTO);


            //Then
            Assertions.assertNotNull(member);
            Mockito.verify(mapperConfig, Mockito.times(1))
                    .mapMemberDTOToMember(memberDTO);
            Mockito.verify(memberRepository, Mockito.times(1))
                    .save(member);
            Mockito.verify(sequenceGeneratorRepository, Mockito.times(1))
                    .generateSequenceByName("members");
        }
    }

    @Nested
    @DisplayName("Login TestCases")
    class LoginTestCases {

        @Test
        @DisplayName("Test for login When correct details are entered")
        void testLogin() {
            Authentication authentication = Mockito.mock(Authentication.class);
            when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(),loginRequestDto.getPassword()))).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(member);
            when(authUtil.generateJwtToken(member)).thenReturn("hello");

            LoginResponseDto responseDto = authService.login(loginRequestDto);

            Assertions.assertNotNull(responseDto);
            Assertions.assertEquals(loginResponseDTO, responseDto);
        }
    }
}