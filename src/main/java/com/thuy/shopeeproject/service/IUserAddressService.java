package com.thuy.shopeeproject.service;

import com.thuy.shopeeproject.domain.entity.UserAddress;

public interface IUserAddressService extends IService<UserAddress, Long> {
    UserAddress findDefaultUserAddressByUserId(Long userId);
}
