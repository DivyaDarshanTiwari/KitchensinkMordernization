package org.mongodb.springboot.kitchensinkmordernization.services;


import org.mongodb.springboot.kitchensinkmordernization.dto.MemberResponseDTO;


import java.util.List;

public interface MemberService {

    MemberResponseDTO getMemberById(Long id);

    List<MemberResponseDTO> findAllMembers();
}
