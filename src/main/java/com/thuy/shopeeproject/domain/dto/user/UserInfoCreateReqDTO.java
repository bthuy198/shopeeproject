package com.thuy.shopeeproject.domain.dto.user;

import java.util.Date;

import com.thuy.shopeeproject.domain.entity.User;
import com.thuy.shopeeproject.domain.entity.UserInfo;
import com.thuy.shopeeproject.domain.enums.EGender;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInfoCreateReqDTO {

    private String name;
    private String phone;
    private String birthday;
    private String gender;

    public UserInfo toUserInfo(User user, Date date) {
        return new UserInfo()
                .setName(name)
                .setBirthday(date)
                .setGender(EGender.getByValue(gender))
                .setPhone(phone)
                .setUser(user);
    }

    public UserInfo toUserInfo(User user) {
        return new UserInfo()
                .setName(name)
                .setGender(EGender.getByValue(gender))
                .setPhone(phone)
                .setUser(user);
    }

    public UserInfo toUserInfo(User user, EGender eGender) {
        return new UserInfo()
                .setName(name)
                .setGender(eGender)
                .setPhone(phone)
                .setUser(user);
    }
}
