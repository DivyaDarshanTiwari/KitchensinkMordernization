package org.mongodb.springboot.kitchensinkmordernization.services;


import org.mongodb.springboot.kitchensinkmordernization.dto.MemberDTO;


import java.util.List;

public interface MemberService {

    MemberDTO getMemberById(Long id);

    List<MemberDTO> findAllMembers();
}
