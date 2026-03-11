package org.mongodb.springboot.kitchensinkmordernization.services.implementation;

import lombok.RequiredArgsConstructor;
import org.mongodb.springboot.kitchensinkmordernization.config.MapperConfig;
import org.mongodb.springboot.kitchensinkmordernization.dto.AdminMemberDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.UpdateMemberRequestByAdminDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberResponseForAdminDto;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.mongodb.springboot.kitchensinkmordernization.enums.MemberRole;
import org.mongodb.springboot.kitchensinkmordernization.repositories.MemberRepository;
import org.mongodb.springboot.kitchensinkmordernization.repositories.custom.SequenceGeneratorRepository;
import org.mongodb.springboot.kitchensinkmordernization.services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final MapperConfig mapperConfig;
    private final SequenceGeneratorRepository sequenceGeneratorRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberResponseForAdminDto updateUserByAdmin(Long id , UpdateMemberRequestByAdminDto updateDto){
        Member member = memberRepository.findById(updateDto.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(!Objects.equals(member.getId(), id) && member.getRole().contains(MemberRole.ADMIN)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to perform edit on another admin"
            );
        }
        member.setEmail(updateDto.getEmail());
        member.setPhoneNumber(updateDto.getPhoneNumber());
        member.setName(updateDto.getName());
        member.setRole(updateDto.getRole());
        return mapperConfig.mapMemberToUpdateMemberResponseByAdmin(memberRepository.save(member));
    }

    @Override
    public void deleteMember(Long adminId, Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(!Objects.equals(member.getId(), adminId) && member.getRole().contains(MemberRole.ADMIN)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to perform delete on another admin"
            );
        }
        memberRepository.deleteById(id);
    }

    @Transactional
    public void signUp(AdminMemberDto memberDTO) {
        Member member = mapperConfig.mapAdminMemberDtoToMember(memberDTO);
        Long sequence = sequenceGeneratorRepository.generateSequenceByName("members");
        member.setId(sequence);
        member.setEmail(memberDTO.getEmail().toLowerCase());
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
    }


}
