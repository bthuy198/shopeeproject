package com.thuy.shopeeproject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCreateResDTO {
    private Long id;
    private String email;
    private String username;
    private String password;
    private String role;
    private UserAvatarDTO userAvatar;
}
