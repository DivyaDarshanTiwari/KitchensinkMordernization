package org.mongodb.springboot.kitchensinkmordernization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.springboot.kitchensinkmordernization.enums.MemberRole;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String jwt;
    Long userId;
    Set<MemberRole> roles;
}
