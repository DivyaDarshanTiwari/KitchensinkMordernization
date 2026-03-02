package org.mongodb.springboot.kitchensinkmordernization.services.implementation;

import lombok.RequiredArgsConstructor;
import org.mongodb.springboot.kitchensinkmordernization.config.MapperConfig;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberResponseDTO;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.mongodb.springboot.kitchensinkmordernization.repositories.MemberRepository;
import org.mongodb.springboot.kitchensinkmordernization.services.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final MapperConfig mapperConfig;

    public MemberResponseDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "The following member related to id: " + id + " was not found"
                ));
        return mapperConfig.mapMemberToMemberResponseDTO(member);
    }


    public List<MemberResponseDTO> findAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(mapperConfig::mapMemberToMemberResponseDTO)
                .toList();
    }

}
