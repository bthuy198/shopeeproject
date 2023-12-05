package com.thuy.shopeeproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thuy.shopeeproject.domain.entity.UserAvatar;
import com.thuy.shopeeproject.repository.UserAvatarRepository;

@Service
@Transactional
public class UserAvatarServiceImpl implements IUserAvatarService {

    @Autowired
    private UserAvatarRepository userAvatarRepository;

    @Override
    public List<UserAvatar> findAll() {
        return userAvatarRepository.findAll();
    }

    @Override
    public Optional<UserAvatar> findById(String id) {
        return userAvatarRepository.findById(id);
    }

    @Override
    public UserAvatar save(UserAvatar e) {
        return userAvatarRepository.save(e);
    }

    @Override
    public void delete(UserAvatar e) {
        userAvatarRepository.delete(e);
    }

    @Override
    public void deleteById(String id) {
        userAvatarRepository.deleteById(id);
    }

}
