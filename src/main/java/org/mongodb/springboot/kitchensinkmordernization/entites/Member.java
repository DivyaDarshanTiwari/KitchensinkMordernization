package org.mongodb.springboot.kitchensinkmordernization.entites;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.mongodb.springboot.kitchensinkmordernization.enums.MemberRole;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Document("members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member implements UserDetails {
    @Id
    @Field("_id")
    private Long id;

    @Field("name")
    @Indexed(unique = true)
    private String name;

    @Field("email")
    @Indexed(unique = true)
    private String email;

    @Field("phone_number")
    private String phoneNumber;

    @Field("password")
    private String password;

    @Field("role")
    private Set<MemberRole> role;

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return
                role.stream()
                        .map(roles -> new SimpleGrantedAuthority("ROLE_" + roles))
                        .collect(Collectors.toSet());
    }

    @Override
    @NullMarked
    public String getUsername() {
        return name;
    }
}
