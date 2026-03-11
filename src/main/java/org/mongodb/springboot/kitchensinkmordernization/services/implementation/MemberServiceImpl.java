package org.mongodb.springboot.kitchensinkmordernization.services.implementation;

import lombok.RequiredArgsConstructor;
import org.mongodb.springboot.kitchensinkmordernization.config.MapperConfig;
import org.mongodb.springboot.kitchensinkmordernization.dto.*;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.mongodb.springboot.kitchensinkmordernization.repositories.MemberRepository;
import org.mongodb.springboot.kitchensinkmordernization.services.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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


    @Override
    public void updateUser(String userId, UpdateMemberRequestDto updateDto) {
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        member.setEmail(updateDto.getEmail());
        member.setPhoneNumber(updateDto.getPhoneNumber());
        member.setName(updateDto.getName());
        memberRepository.save(member);
    }

    public PaginatedResponseDto<MemberResponseForAdminDto> getAllUsersPaginated(
            int page,
            int size,
            String sortBy,
            String direction,
            String lastSortValue,
            Long lastId,
            String searchQuery) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Object typedLastSortValue = convertSortValue(sortBy, lastSortValue);

        Page<Member> memberPage = memberRepository.findAll(
                sortBy,
                sortDirection,
                typedLastSortValue,
                lastId,
                size,
                page,
                searchQuery
        );

        List<MemberResponseForAdminDto> content = memberPage.getContent()
                .stream()
                .map(mapperConfig::toAdminDto)
                .toList();

        String newLastSortValue = null;
        Long newLastId = null;
        if (!memberPage.getContent()
                .isEmpty()) {
            Member lastMember = memberPage.getContent()
                    .get(memberPage.getContent()
                            .size() - 1);
            newLastSortValue = getFieldValue(lastMember, sortBy);
            newLastId = lastMember.getId();
        }

        return PaginatedResponseDto.<MemberResponseForAdminDto>builder()
                .content(content)
                .currentPage(memberPage.getNumber())
                .totalPages(memberPage.getTotalPages())
                .totalElements(memberPage.getTotalElements())
                .pageSize(memberPage.getSize())
                .hasNext(memberPage.hasNext())
                .hasPrevious(memberPage.hasPrevious())
                .lastSortValue(newLastSortValue)
                .lastId(newLastId)
                .build();
    }

    private Object convertSortValue(String sortBy, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        if (sortBy.equals("id")) {
            return Long.parseLong(value);
        }

        return value;
    }

    private String getFieldValue(Member member, String fieldName) {
        return switch (fieldName) {
            case "id" -> member.getId()
                    .toString();
            case "name" -> member.getName();
            case "email" -> member.getEmail();
            case "role" -> member.getRole()
                    .toString();
            case "phoneNumber" -> member.getPhoneNumber();
            default -> member.getId()
                    .toString();
        };
    }


}
