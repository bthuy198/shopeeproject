package com.thuy.shopeeproject.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thuy.shopeeproject.domain.dto.user.UserInfoCreateReqDTO;
import com.thuy.shopeeproject.domain.entity.User;
import com.thuy.shopeeproject.domain.entity.UserInfo;
import com.thuy.shopeeproject.repository.UserInfoRepository;
import com.thuy.shopeeproject.repository.UserRepository;
import com.thuy.shopeeproject.utils.AppUtils;

@Service
@Transactional
public class UserInfoServiceImpl implements IUserInfoService {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private AppUtils appUtils;

    @Override
    public List<UserInfo> findAll() {
        return userInfoRepository.findAll();
    }

    @Override
    public Optional<UserInfo> findById(Long id) {
        return userInfoRepository.findById(id);
    }

    @Override
    public UserInfo save(UserInfo e) {
        return userInfoRepository.save(e);
    }

    @Override
    public void delete(UserInfo e) {
        e.setDeleted(true);
        userInfoRepository.save(e);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public UserInfo getUserInfoByUserId(Long userId) {
        return userInfoRepository.getUserInfoByUserId(userId);
    }

    @Override
    public UserInfo createUserInfo(UserInfoCreateReqDTO userInfoCreateReqDTO, User user) {
        Date birthday = appUtils.parseStringToDate(userInfoCreateReqDTO.getBirthday());
        UserInfo userInfo = userInfoCreateReqDTO.toUserInfo(user, birthday);
        userInfo = userInfoRepository.save(userInfo);
        user.setUserInfo(userInfo);
        userService.save(user);
        return userInfo;
    }

    @Override
    public Boolean existsByPhone(String phone) {
        return userInfoRepository.existsByPhone(phone);
    }

}
