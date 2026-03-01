package org.mongodb.springboot.kitchensinkmordernization.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    @NotBlank
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[a-zA-Z0-9_\\s]+", message = "Must not contain numbers")
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 10, max = 12)
    @Digits(fraction = 0, integer = 12)
    private String phoneNumber;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
