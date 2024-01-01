package com.thuy.shopeeproject.service;

import com.thuy.shopeeproject.domain.dto.user.UserInfoCreateReqDTO;
import com.thuy.shopeeproject.domain.entity.User;
import com.thuy.shopeeproject.domain.entity.UserInfo;

public interface IUserInfoService extends IService<UserInfo, Long> {
    UserInfo getUserInfoByUserId(Long userId);

    UserInfo createUserInfo(UserInfoCreateReqDTO userInfoCreateReqDTO, User user);

    Boolean existsByPhone(String phone);
}
