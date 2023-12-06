package com.thuy.shopeeproject.service;

import com.thuy.shopeeproject.domain.dto.UserCreateReqDTO;
import com.thuy.shopeeproject.domain.entity.User;

public interface IUserService extends IService<User, Long> {
    User createNoAvatar(User user);

    User createWithAvatar(UserCreateReqDTO userCreateReqDTO, User user);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
