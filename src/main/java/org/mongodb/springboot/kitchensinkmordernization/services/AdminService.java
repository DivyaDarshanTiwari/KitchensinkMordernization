package org.mongodb.springboot.kitchensinkmordernization.services;

import jakarta.validation.constraints.NotBlank;
import org.mongodb.springboot.kitchensinkmordernization.dto.AdminMemberDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberResponseForAdminDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.UpdateMemberRequestByAdminDto;

public interface AdminService {
    MemberResponseForAdminDto updateUserByAdmin(Long id, UpdateMemberRequestByAdminDto memberDTO);

    void deleteMember(Long adminId, @NotBlank Long id);

    void signUp(AdminMemberDto memberDTO);
}
