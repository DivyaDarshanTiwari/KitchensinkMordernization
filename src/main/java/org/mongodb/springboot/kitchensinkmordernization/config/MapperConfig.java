package org.mongodb.springboot.kitchensinkmordernization.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberDTO;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;


@Mapper(componentModel = "spring")
public interface MapperConfig {

    MemberDTO mapMemberToMemberDTO(Member member);

    @Mapping(target="id",ignore = true)
    @Mapping(target="role",ignore = true)
    Member mapMemberDTOToMember(MemberDTO memberDTO);

}
