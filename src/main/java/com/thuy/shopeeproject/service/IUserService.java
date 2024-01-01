package com.thuy.shopeeproject.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.thuy.shopeeproject.domain.dto.UserCreateReqDTO;
import com.thuy.shopeeproject.domain.entity.User;

public interface IUserService extends IService<User, Long>, UserDetailsService {
    User createNoAvatar(User user);

    User createWithAvatar(UserCreateReqDTO userCreateReqDTO, User user);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
}
