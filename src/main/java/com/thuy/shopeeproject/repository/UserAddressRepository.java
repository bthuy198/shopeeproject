package com.thuy.shopeeproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thuy.shopeeproject.domain.entity.UserAddress;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    @Query(value = "SELECT * FROM user_address WHERE users_id = :userId AND is_default = 1", nativeQuery = true)
    UserAddress findDefaultUserAddressByUserId(@Param("userId") Long userId);
}
