package org.mongodb.springboot.kitchensinkmordernization.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mongodb.springboot.kitchensinkmordernization.dto.AdminMemberDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberDTO;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberResponseDTO;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberResponseForAdminDto;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;


@Mapper(componentModel = "spring")
public interface MapperConfig {


    MemberResponseDTO mapMemberToMemberResponseDTO(Member member);

    @Mapping(target="id",ignore = true)
    @Mapping(target="role",ignore = true)
    Member mapMemberDTOToMember(MemberDTO memberDTO);

    MemberResponseForAdminDto mapMemberToUpdateMemberResponseByAdmin(Member member);

    Member mapAdminMemberDtoToMember(AdminMemberDto adminMemberDto);

    @Mapping(source = "name", target = "name")
    MemberResponseForAdminDto toAdminDto(Member member);

}
