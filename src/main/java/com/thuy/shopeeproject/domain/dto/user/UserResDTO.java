package com.thuy.shopeeproject.domain.dto.user;

import com.thuy.shopeeproject.domain.dto.UserAvatarDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private UserInfoResDTO userInfo;
    private UserAvatarDTO userAvatar;
}
