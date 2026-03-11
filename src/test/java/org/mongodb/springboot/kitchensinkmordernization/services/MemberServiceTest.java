package org.mongodb.springboot.kitchensinkmordernization.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.mongodb.springboot.kitchensinkmordernization.config.MapperConfig;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberResponseDTO;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.mongodb.springboot.kitchensinkmordernization.enums.MemberRole;
import org.mongodb.springboot.kitchensinkmordernization.repositories.MemberRepository;
import org.mongodb.springboot.kitchensinkmordernization.services.implementation.MemberServiceImpl;
import org.springframework.web.server.ResponseStatusException;

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

    @Nested
    @DisplayName("All the test for findById")
    class GetMemberByIdClass {
        @Test
        void getMemberById_Valid_Id() {
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
        void getMemberById_Invalid_Id() {
            assertThatThrownBy(() -> memberService.getMemberById(2L)).isInstanceOf(ResponseStatusException.class);

        }

    }


//    @Nested
//    @DisplayName("All the test for findAll")
//    class FindAllMembersClass {
//        @Test
//        void findAllMembers_whenDataIsPresent() {
//            //Given
//            List<Member> membersList = List.of(member);
//            Pageable pageable = PageRequest.of(0, 10);
//            Page<Member> memberPage = new PageImpl(membersList, pageable, membersList.size());
//            when(memberRepository.findAll(any(Pageable.class))).thenReturn(memberPage);
//            when(mapperConfig.mapMemberToMemberResponseDTO(member)).thenReturn(memberResponseDTO);
//            Page<MemberResponseDTO> memberResponseDTOPage = new  PageImpl(memberResponseDTOList, pageable, memberResponseDTOList.size());
//            //When
//            Page<MemberResponseDTO> responseDTOList = memberService.findAllMembers(0,10);
//
//            //then
//            Assertions.assertEquals(memberResponseDTOPage, responseDTOList);
//
//        }
//
//        @Test
//        void findAllMembers_whenDataIsNotPresent() {
//            //Given
//            List<Member> emptyMembersList = List.of();
//            Pageable pageable = PageRequest.of(0, 10);
//            Page<Member> memberPage = new PageImpl(emptyMembersList, pageable, emptyMembersList.size());
//            Page<Member> responseMemberPage = new PageImpl(emptyMembersList, pageable, emptyMembersList.size());
//            when(memberRepository.findAll(any(Pageable.class))).thenReturn(responseMemberPage);
//
//            //When
//            Page<MemberResponseDTO> responseDTOList = memberService.findAllMembers(0,10);
//
//            //then
//            Assertions.assertIterableEquals(memberPage, responseDTOList);
//
//        }
//
//    }

}