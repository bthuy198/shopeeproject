package com.thuy.shopeeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thuy.shopeeproject.domain.entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    @Query("SELECT u.userInfo FROM User u WHERE u.id = :userId")
    UserInfo getUserInfoByUserId(@Param("userId") Long userId);

    Boolean existsByPhone(String phone);
}
