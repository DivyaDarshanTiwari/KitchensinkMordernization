package org.mongodb.springboot.kitchensinkmordernization.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mongodb.springboot.kitchensinkmordernization.config.MapperConfig;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberResponseDTO;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.mongodb.springboot.kitchensinkmordernization.enums.MemberRole;
import org.mongodb.springboot.kitchensinkmordernization.repositories.MemberRepository;
import org.mongodb.springboot.kitchensinkmordernization.services.implementation.MemberServiceImpl;

import java.util.*;

import static org.mockito.Mockito.when;


@Slf4j
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MapperConfig mapperConfig;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member member;
    private Long memberId;
    private MemberResponseDTO memberResponseDTO;
    private List<MemberResponseDTO> memberResponseDTOList;

    @BeforeEach
    void init() {

        log.info("Before Each");

        this.member = Member.builder()
                .id(1L)
                .name("Divya")
                .email("divya@gmail.com")
                .phoneNumber("1111111111")
                .password("")
                .role(Set.of(MemberRole.MEMBER))
                .build();
        this.memberResponseDTO = MemberResponseDTO.builder()
                .id(1L)
                .name("Divya")
                .email("divya@gmail.com")
                .phoneNumber("1111111111")
                .build();
        this.memberId = 1L;
        this.memberResponseDTOList = List.of(memberResponseDTO);


    }

    @Test
    @DisplayName("Find Member By ID")
    void getMemberById() {
        //Given
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(mapperConfig.mapMemberToMemberResponseDTO(member)).thenReturn(memberResponseDTO);

        //When
        MemberResponseDTO responseDTO = memberService.getMemberById(memberId);

        //then

        Assertions.assertEquals(responseDTO, memberResponseDTO);
        Mockito.verify(memberRepository, Mockito.times(1))
                .findById(memberId);
    }

    @Test
    @DisplayName("Find all the Members")
    void findAllMembers() {
        //Given
        List<Member> membersList = List.of(member);
        when(memberRepository.findAll()).thenReturn(membersList);
        when(mapperConfig.mapMemberToMemberResponseDTO(member)).thenReturn(memberResponseDTO);


        log.info("Find all the Members");
        //When
        List<MemberResponseDTO> responseDTOList = memberService.findAllMembers();
        //then

        Assertions.assertEquals(memberResponseDTOList, responseDTOList);

    }
}