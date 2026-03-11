package org.mongodb.springboot.kitchensinkmordernization.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.springboot.kitchensinkmordernization.enums.MemberRole;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseForAdminDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Set<MemberRole> role;
}
