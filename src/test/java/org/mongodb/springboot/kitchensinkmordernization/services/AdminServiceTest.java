package org.mongodb.springboot.kitchensinkmordernization.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mongodb.springboot.kitchensinkmordernization.config.MapperConfig;
import org.mongodb.springboot.kitchensinkmordernization.dto.*;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.mongodb.springboot.kitchensinkmordernization.enums.MemberRole;
import org.mongodb.springboot.kitchensinkmordernization.repositories.MemberRepository;
import org.mongodb.springboot.kitchensinkmordernization.repositories.custom.SequenceGeneratorRepository;
import org.mongodb.springboot.kitchensinkmordernization.services.implementation.AdminServiceImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Spy
    private MapperConfig mapperConfig = Mappers.getMapper(MapperConfig.class);
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private SequenceGeneratorRepository sequenceGeneratorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Member member;
    private UpdateMemberRequestByAdminDto updateDto;
    private Member updatedMember;
    private MemberResponseForAdminDto expectedResponse;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("010-1234-5678")
                .password("password123")
                .role(Set.of(MemberRole.MEMBER))
                .build();

        updateDto = UpdateMemberRequestByAdminDto.builder()
                .id(1L)
                .name("John Updated")
                .email("john.updated@example.com")
                .phoneNumber("010-9999-8888")
                .role(Set.of(MemberRole.ADMIN))
                .build();
        updatedMember = Member.builder()
                .id(1L)
                .name("John Updated")
                .email("john.updated@example.com")
                .phoneNumber("010-9999-8888")
                .password("password123")
                .role(Set.of(MemberRole.ADMIN))
                .build();

        expectedResponse = MemberResponseForAdminDto.builder()
                .id(1L)
                .name("John Updated")
                .email("john.updated@example.com")
                .phoneNumber("010-9999-8888")
                .role(Set.of(MemberRole.ADMIN))
                .build();

    }


    @Nested
    class updateUserByAdmin {

        @Test
        void adminService_updateUserByAdmin_Successful() {

            when(memberRepository.findById(1L)).thenThrow(UsernameNotFoundException.class);

            Assertions.assertThrows(UsernameNotFoundException.class, () -> {
                adminService.updateUserByAdmin(1L, updateDto);
            });
            verify(memberRepository, times(1)).findById(1L);

        }

        @Test
        void adminService_updateUserByAdmin_IdNotFound() {

            when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
            when(memberRepository.save(member)).thenReturn(updatedMember);
            when(mapperConfig.mapMemberToUpdateMemberResponseByAdmin(updatedMember)).thenReturn(expectedResponse);

            MemberResponseForAdminDto actualResponse = adminService.updateUserByAdmin(1L, updateDto);

            Assertions.assertEquals(expectedResponse, actualResponse);
            Assertions.assertNotNull(actualResponse);
            Assertions.assertEquals(updateDto.getName(), actualResponse.getName());
            Assertions.assertEquals(updateDto.getEmail(), actualResponse.getEmail());
            Assertions.assertEquals(updateDto.getPhoneNumber(), actualResponse.getPhoneNumber());
            Assertions.assertEquals(updateDto.getRole(), actualResponse.getRole());

            verify(memberRepository, times(1)).findById(1L);
            verify(memberRepository, times(1)).save(member);

        }

        @Test
        void adminService_updateUserByAdmin_AdminCannotEditAdmin() {
            Member memberAdmin = Member.builder()
                    .id(1L)
                    .name("John Updated")
                    .email("john.updated@example.com")
                    .phoneNumber("010-9999-8888")
                    .password("password123")
                    .role(Set.of(MemberRole.ADMIN))
                    .build();
            when(memberRepository.findById(1L)).thenReturn(Optional.of(memberAdmin));

            Assertions.assertThrows(ResponseStatusException.class, () -> {
                adminService.updateUserByAdmin(3L, updateDto);
            });
            verify(memberRepository, times(1)).findById(1L);

        }
    }

    @Nested
    class deleteMember {
        @Test
        void adminService_deleteMember_Successfully() {
            when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
            doNothing().when(memberRepository)
                    .deleteById(1L);

            adminService.deleteMember(5L, 1L);
            verify(memberRepository, times(1)).findById(1L);
            verify(memberRepository, times(1)).deleteById(1L);
        }

        @Test
        void adminService_deleteMember_AdminNotAbleToDeleteAnotherAdmin_Successfully() {
            Member memberAdmin = Member.builder()
                    .id(5L)
                    .name("John Updated")
                    .email("john.updated@example.com")
                    .phoneNumber("010-9999-8888")
                    .password("password123")
                    .role(Set.of(MemberRole.ADMIN))
                    .build();
            when(memberRepository.findById(5L)).thenReturn(Optional.of(memberAdmin));

            Assertions.assertThrows(ResponseStatusException.class, () -> {
                adminService.deleteMember(1L, 5L);
            });
        }

        @Test
        void adminService_deleteMemberHimself_Successfully() {
            Member memberAdmin = Member.builder()
                    .id(1L)
                    .name("John Updated")
                    .email("john.updated@example.com")
                    .phoneNumber("010-9999-8888")
                    .password("password123")
                    .role(Set.of(MemberRole.ADMIN))
                    .build();
            when(memberRepository.findById(1L)).thenReturn(Optional.of(memberAdmin));
            doNothing().when(memberRepository)
                    .deleteById(1L);

            adminService.deleteMember(1L, 1L);

            verify(memberRepository, times(1)).findById(1L);
            verify(memberRepository, times(1)).deleteById(1L);
        }


    }

    @Nested
    class signUp {

        @Test
        void adminService_signUp_Successfully() {
            AdminMemberDto memberDto = AdminMemberDto.builder()
                    .name("John Doe")
                    .email("john.doe@example.com")
                    .phoneNumber("010-1234-5678")
                    .password("password123")
                    .role(Set.of(MemberRole.MEMBER))
                    .build();
            when(mapperConfig.mapAdminMemberDtoToMember(memberDto)).thenReturn(member);
            when(sequenceGeneratorRepository.generateSequenceByName("members")).thenReturn(1L);
            when(memberRepository.save(member)).thenReturn(member);

            adminService.signUp(memberDto);

            verify(sequenceGeneratorRepository, times(1)).generateSequenceByName("members");
            verify(memberRepository, times(1)).save(member);
        }

    }
}