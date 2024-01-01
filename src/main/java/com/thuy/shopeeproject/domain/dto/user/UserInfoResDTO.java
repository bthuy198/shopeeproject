package com.thuy.shopeeproject.domain.dto.user;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInfoResDTO {
    private Long id;
    private String name;
    private String phone;
    private Date birthday;
    private String gender;
}
