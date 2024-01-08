package com.thuy.shopeeproject.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuy.shopeeproject.domain.dto.DefaulUserAddressDTO;
import com.thuy.shopeeproject.domain.dto.user.UserAddressDTO;
import com.thuy.shopeeproject.domain.dto.user.UserInfoResDTO;
import com.thuy.shopeeproject.domain.entity.User;
import com.thuy.shopeeproject.domain.entity.UserAddress;
import com.thuy.shopeeproject.domain.entity.UserInfo;
import com.thuy.shopeeproject.exceptions.CustomErrorException;
import com.thuy.shopeeproject.service.IUserAddressService;
import com.thuy.shopeeproject.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api")
public class UserAddressAPI {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserAddressService userAddressService;

    @GetMapping("admin/users/address/get-all")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> getAllUserAddress() {
        List<UserAddress> userAddressList = userAddressService.findAll();
        List<UserAddressDTO> userAddressDTOs = new ArrayList<>();
        for (UserAddress userAddress : userAddressList) {
            UserAddressDTO userAddressResDTO = userAddress.toUserAddressDTO();
            userAddressDTOs.add(userAddressResDTO);
        }
        return new ResponseEntity<>(userAddressDTOs, HttpStatus.OK);
    }

    @GetMapping("/users/address")
    public ResponseEntity<?> getUserAddress(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Please sign in to order");
        }

        User user = optionalUser.get();
        List<UserAddress> userAddressList = user.getUserAddresses();
        List<UserAddressDTO> userAddressDTOs = new ArrayList<>();
        for (UserAddress userAddress : userAddressList) {
            UserAddressDTO userAddressResDTO = userAddress.toUserAddressDTO();
            userAddressDTOs.add(userAddressResDTO);
        }
        return new ResponseEntity<>(userAddressDTOs, HttpStatus.OK);
    }

    @GetMapping("admin/users/address")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> getUserAddressByUserId(@RequestParam("userId") Long userId) {

        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found user adrress of this user");
        }

        User user = optionalUser.get();
        List<UserAddress> userAddressList = user.getUserAddresses();
        List<UserAddressDTO> userAddressDTOs = new ArrayList<>();
        for (UserAddress userAddress : userAddressList) {
            UserAddressDTO userAddressResDTO = userAddress.toUserAddressDTO();
            userAddressDTOs.add(userAddressResDTO);
        }
        return new ResponseEntity<>(userAddressDTOs, HttpStatus.OK);
    }

    @PostMapping("/users/address")
    @PreAuthorize("hasAnyAuthority('user')")
    public ResponseEntity<?> createUserAddress(@RequestBody UserAddressDTO userAddressDTO, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Please sign in to order");
        }

        User user = optionalUser.get();

        UserAddress userAddress = new UserAddress();
        userAddress.setUser(user);
        userAddress.setAddress(userAddressDTO.getAddress());
        userAddress.setName(userAddressDTO.getName());
        userAddress.setPhone(userAddressDTO.getPhone());

        List<UserAddress> userAddressList = user.getUserAddresses();

        if (userAddressList.size() == 0) {
            userAddress.setIsDefault(true);
        }

        userAddressList.add(userAddress);

        userAddress = userAddressService.save(userAddress);
        user.setUserAddresses(userAddressList);
        user = userService.save(user);

        return new ResponseEntity<>(userAddress.toUserAddressDTO(), HttpStatus.CREATED);
    }

    @PostMapping("users/address/set_default_address")
    @PreAuthorize("hasAnyAuthority('user')")
    public ResponseEntity<?> setDefaultAddress(@RequestBody DefaulUserAddressDTO defaulUserAddressDTO,
            HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Please sign in to order");
        }

        User user = optionalUser.get();

        Optional<UserAddress> optionalAddress = userAddressService.findById(defaulUserAddressDTO.getAddressId());
        if (!optionalAddress.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this address");
        }

        UserAddress userAddress = optionalAddress.get();
        if (user.getId() != userAddress.getUser().getId()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "You do not have permission to do this");
        }

        UserAddress defaultAddress = userAddressService.findDefaultUserAddressByUserId(userId);
        defaultAddress.setIsDefault(false);
        defaultAddress = userAddressService.save(defaultAddress);

        userAddress.setIsDefault(true);
        userAddress = userAddressService.save(userAddress);

        return new ResponseEntity<>(userAddress.toUserAddressDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/users/address")
    @PreAuthorize("hasAnyAuthority('user')")
    public ResponseEntity<?> deleteUserAddress(@RequestParam("userAddressId") Long userAddressId,
            HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Please sign in to delete address");
        }

        Optional<UserAddress> optionalUserAddress = userAddressService.findById(userAddressId);
        if (!optionalUserAddress.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this user address");
        }

        userAddressService.deleteUserAddress(optionalUser.get(), optionalUserAddress.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
