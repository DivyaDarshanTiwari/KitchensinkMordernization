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
public class AdminMemberDto {
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 25)
    @Pattern(regexp = "^\\D+$" , message = "Must  not contain a number")
    private String name;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotEmpty
    @Size(min = 10, max = 12, message = "Phone number size must be between 10 and 12")
    @Digits(fraction = 0, integer = 12, message = "Phone number should be a digit")
    private String phoneNumber;

    @NotNull(message = "Role cannot be null")
    @NotEmpty(message = "Role cannot be empty")
    private Set<MemberRole> role;

    @NotNull
    @NotEmpty
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}", message = "Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character")
    private String password;
}
