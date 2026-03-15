package org.mongodb.springboot.kitchensinkmordernization.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.mongodb.springboot.kitchensinkmordernization.config.MapperConfig;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberResponseDTO;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberResponseForAdminDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.PaginatedResponseDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.UpdateMemberRequestDto;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.mongodb.springboot.kitchensinkmordernization.enums.MemberRole;
import org.mongodb.springboot.kitchensinkmordernization.repositories.MemberRepository;
import org.mongodb.springboot.kitchensinkmordernization.services.implementation.MemberServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


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


    }

    @Nested
    @DisplayName("All the test for findById")
    class GetMemberByIdClass {
        @Test
        void getMemberById_Valid_Id() {
            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(mapperConfig.mapMemberToMemberResponseDTO(member)).thenReturn(memberResponseDTO);

            MemberResponseDTO responseDTO = memberService.getMemberById(memberId);

            Assertions.assertEquals(responseDTO, memberResponseDTO);
            verify(memberRepository, Mockito.times(1))
                    .findById(memberId);
        }

        @Test
        void getMemberById_Invalid_Id() {
            Assertions.assertThrows(ResponseStatusException.class, () -> memberService.getMemberById(2L));

        }

    }

    @Nested
    @DisplayName("All the test for updateUser")
    class UpdateUserClass {

        @Test
        void updateUser_Success() {
            String userId = "1";
            UpdateMemberRequestDto updateDto = UpdateMemberRequestDto.builder()
                    .name("Updated Name")
                    .email("updated@gmail.com")
                    .phoneNumber("2222222222")
                    .build();

            when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
            when(memberRepository.save(member)).thenReturn(member);

            memberService.updateUser(userId, updateDto);

            verify(memberRepository, times(1)).findById(1L);
            verify(memberRepository, times(1)).save(member);
        }

        @Test
        void updateUser_UserNotFound() {
            String userId = "999";
            UpdateMemberRequestDto updateDto = UpdateMemberRequestDto.builder()
                    .name("Updated Name")
                    .email("updated@gmail.com")
                    .phoneNumber("2222222222")
                    .build();

            when(memberRepository.findById(999L)).thenReturn(Optional.empty());

            Assertions.assertThrows(UsernameNotFoundException.class, () -> memberService.updateUser(userId, updateDto));
            verify(memberRepository).findById(999L);
            verify(memberRepository, never()).save(any());
        }
    }

    @Nested
    class GetAllUsersPaginatedClass {

        private Member member2;
        private MemberResponseForAdminDto adminDto1;
        private MemberResponseForAdminDto adminDto2;
        private List<Member> members;

        @BeforeEach
        void setUp() {
            member2 = Member.builder()
                    .id(2L)
                    .name("Rahul")
                    .email("rahul@gmail.com")
                    .phoneNumber("2222222222")
                    .password("")
                    .role(Set.of(MemberRole.MEMBER))
                    .build();

            adminDto1 = MemberResponseForAdminDto.builder()
                    .id(1L)
                    .name("Divya")
                    .email("divya@gmail.com")
                    .phoneNumber("1111111111")
                    .build();

            adminDto2 = MemberResponseForAdminDto.builder()
                    .id(2L)
                    .name("Rahul")
                    .email("rahul@gmail.com")
                    .phoneNumber("2222222222")
                    .build();

            members = List.of(member, member2);
        }

        @Test
        void getAllUsers_AscendingOrder_Success() {
            Page<Member> memberPage = new PageImpl<>(members);
            when(
                    memberRepository.findAll(
                            eq("name"),
                            eq(Sort.Direction.ASC),
                            isNull(),
                            isNull(),
                            eq(10),
                            eq(0),
                            isNull()
                    )
            ).thenReturn(memberPage);

            when(mapperConfig.toAdminDto(member)).thenReturn(adminDto1);
            when(mapperConfig.toAdminDto(member2)).thenReturn(adminDto2);

            PaginatedResponseDto<MemberResponseForAdminDto> result = memberService.getAllUsersPaginated(
                    0, 10, "name", "asc", null, null, null
            );

            Assertions.assertNotNull(result);
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getContent()
                    .get(0)
                    .getName()).isEqualTo("Divya");
            assertThat(result.getContent()
                    .get(1)
                    .getName()).isEqualTo("Rahul");
            assertThat(result.getLastSortValue()).isEqualTo("Rahul");
        }

        @Test
        void getAllUsers_DescendingOrder_Success() {
            List<Member> membersDesc = List.of(member2, member);
            Page<Member> memberPage = new PageImpl<>(membersDesc);
            when(
                    memberRepository.findAll(
                            eq("name"),
                            eq(Sort.Direction.DESC),
                            isNull(),
                            isNull(),
                            eq(10),
                            eq(0),
                            isNull()
                    )
            ).thenReturn(memberPage);

            when(mapperConfig.toAdminDto(member)).thenReturn(adminDto1);
            when(mapperConfig.toAdminDto(member2)).thenReturn(adminDto2);

            PaginatedResponseDto<MemberResponseForAdminDto> result = memberService.getAllUsersPaginated(
                    0, 10, "name", "desc", null, null, null
            );

            Assertions.assertNotNull(result);
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getContent()
                    .get(1)
                    .getName()).isEqualTo("Divya");
            assertThat(result.getContent()
                    .get(0)
                    .getName()).isEqualTo("Rahul");
            assertThat(result.getLastSortValue()).isEqualTo("Divya");
        }

        @Test
        void getAllUsers_EmptyResult() {
            Page<Member> emptyPage = new PageImpl<>(List.of());

            when(memberRepository.findAll(
                    anyString(),
                    any(Sort.Direction.class),
                    any(),
                    any(),
                    anyInt(),
                    anyInt(),
                    any()
            )).thenReturn(emptyPage);

            PaginatedResponseDto<MemberResponseForAdminDto> result = memberService.getAllUsersPaginated(
                    0, 10, "name", "asc", null, null, null
            );

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getLastSortValue()).isNull();
            assertThat(result.getLastId()).isNull();
        }

        @Test
        void getAllUsers_WithIdSort_ConvertsLastSortValueToLong() {
            Page<Member> memberPage = new PageImpl<>(members);

            when(memberRepository.findAll(
                    eq("id"),
                    eq(Sort.Direction.ASC),
                    eq(100L),
                    eq(100L),
                    eq(10),
                    eq(0),
                    isNull()
            )).thenReturn(memberPage);
            when(mapperConfig.toAdminDto(member)).thenReturn(adminDto1);
            when(mapperConfig.toAdminDto(member2)).thenReturn(adminDto2);

            PaginatedResponseDto<MemberResponseForAdminDto> result = memberService.getAllUsersPaginated(
                    0, 10, "id", "asc", "100", 100L, null
            );

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
        }

        @Test
        void getAllUsers_WithEmailSort_GetsEmailFieldValue() {
            Page<Member> memberPage = new PageImpl<>(members);

            when(memberRepository.findAll(
                    eq("email"),
                    eq(Sort.Direction.ASC),
                    isNull(),
                    isNull(),
                    eq(10),
                    eq(0),
                    isNull()
            )).thenReturn(memberPage);
            when(mapperConfig.toAdminDto(member)).thenReturn(adminDto1);
            when(mapperConfig.toAdminDto(member2)).thenReturn(adminDto2);

            PaginatedResponseDto<MemberResponseForAdminDto> result = memberService.getAllUsersPaginated(
                    0, 10, "email", "asc", null, null, null
            );

            assertThat(result).isNotNull();
            assertThat(result.getLastSortValue()).isEqualTo("rahul@gmail.com");
            assertThat(result.getLastId()).isEqualTo(2L);
        }

        @Test
        void getAllUsers_WithSearchQuery() {
            Page<Member> memberPage = new PageImpl<>(List.of(member));

            when(memberRepository.findAll(
                    eq("name"),
                    eq(Sort.Direction.ASC),
                    isNull(),
                    isNull(),
                    eq(10),
                    eq(0),
                    eq("div")
            )).thenReturn(memberPage);
            when(mapperConfig.toAdminDto(member)).thenReturn(adminDto1);

            PaginatedResponseDto<MemberResponseForAdminDto> result = memberService.getAllUsersPaginated(
                    0, 10, "name", "asc", null, null, "div"
            );

            Assertions.assertEquals(result.getContent()
                    .get(0), adminDto1);
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(memberRepository, times(1)).findAll("name", Sort.Direction.ASC, null, null, 10, 0, "div");
        }
    }

}