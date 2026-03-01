package org.mongodb.springboot.kitchensinkmordernization.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    private String name;
    private String password;

}
