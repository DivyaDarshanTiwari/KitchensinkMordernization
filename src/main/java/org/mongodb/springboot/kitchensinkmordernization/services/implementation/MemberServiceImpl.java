package org.mongodb.springboot.kitchensinkmordernization.services.implementation;

import lombok.RequiredArgsConstructor;
import org.mongodb.springboot.kitchensinkmordernization.config.MapperConfig;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberDTO;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.mongodb.springboot.kitchensinkmordernization.repositories.MemberRepository;
import org.mongodb.springboot.kitchensinkmordernization.repositories.custom.SequenceGeneratorRepository;
import org.mongodb.springboot.kitchensinkmordernization.services.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final SequenceGeneratorRepository sequenceGeneratorRepository;

    private final MapperConfig mapperConfig;

    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "The following member related to id: " + id + " was not found"
                ));
        return mapperConfig.mapMemberToMemberDTO(member);
    }


    public List<MemberDTO> findAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(mapperConfig::mapMemberToMemberDTO)
                .toList();
    }

    public void createMember(MemberDTO memberDTO) {
        Member member = mapperConfig.mapMemberDTOToMember(memberDTO);
        Long sequence = sequenceGeneratorRepository.generateSequenceByName("members");
        member.setId(sequence);
        memberRepository.save(member);
    }
}
