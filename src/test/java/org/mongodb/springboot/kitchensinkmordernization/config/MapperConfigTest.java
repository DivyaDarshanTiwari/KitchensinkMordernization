package org.mongodb.springboot.kitchensinkmordernization.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberResponseDTO;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.mongodb.springboot.kitchensinkmordernization.enums.MemberRole;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
class MapperConfigTest {

    private final MapperConfig mapperConfig = Mappers.getMapper(MapperConfig.class);

    private final Member member = Member.builder().name("Divya").email("d@gmail.com").phoneNumber("1111111111").password("Hello").role(Set.of(MemberRole.MEMBER)).build();
    private final MemberResponseDTO memberResponseDTO = MemberResponseDTO.builder().name("Divya").email("d@gmail.com").phoneNumber("1111111111").build();

    @Test
    void mapMemberToMemberResponseDTO() {
        MemberResponseDTO response =  mapperConfig.mapMemberToMemberResponseDTO(member);

        Assertions.assertEquals(memberResponseDTO, response);
    }
}