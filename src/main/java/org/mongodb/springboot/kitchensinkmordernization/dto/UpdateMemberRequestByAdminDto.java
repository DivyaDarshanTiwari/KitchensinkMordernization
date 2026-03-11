package org.mongodb.springboot.kitchensinkmordernization.dto;

import jakarta.validation.constraints.*;
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
public class UpdateMemberRequestByAdminDto {

    @NotNull
    private Long id;


    @NotBlank
    @Size(min = 1, max = 25)
    @Pattern(regexp = "^\\D+$" , message = "Must  not contain a number")
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 10, max = 12, message = "Phone number size must be between 10 and 12")
    @Digits(fraction = 0, integer = 12, message = "Phone number should be a digit")
    private String phoneNumber;

    private Set<MemberRole> role;
}
