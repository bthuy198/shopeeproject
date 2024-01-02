package com.thuy.shopeeproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thuy.shopeeproject.domain.entity.UserAddress;
import com.thuy.shopeeproject.repository.UserAddressRepository;

@Service
@Transactional
public class UserAddressImpl implements IUserAddressService {

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Override
    public List<UserAddress> findAll() {
        return userAddressRepository.findAll();
    }

    @Override
    public Optional<UserAddress> findById(Long id) {
        return userAddressRepository.findById(id);
    }

    @Override
    public UserAddress save(UserAddress e) {
        return userAddressRepository.save(e);
    }

    @Override
    public void delete(UserAddress e) {
        e.setIsDefault(true);
        e = userAddressRepository.save(e);
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public UserAddress findDefaultUserAddressByUserId(Long userId) {
        return userAddressRepository.findDefaultUserAddressByUserId(userId);
    }

}
