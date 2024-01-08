package com.thuy.shopeeproject.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuy.shopeeproject.domain.dto.product.ProductFilterReqDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductResDTO;
import com.thuy.shopeeproject.domain.dto.user.UserFilterReqDTO;
import com.thuy.shopeeproject.domain.dto.user.UserInfoCreateReqDTO;
import com.thuy.shopeeproject.domain.dto.user.UserInfoResDTO;
import com.thuy.shopeeproject.domain.dto.user.UserInfoUpdateReqDTO;
import com.thuy.shopeeproject.domain.dto.user.UserResDTO;
import com.thuy.shopeeproject.domain.entity.User;
import com.thuy.shopeeproject.domain.entity.UserInfo;
import com.thuy.shopeeproject.exceptions.CustomErrorException;
import com.thuy.shopeeproject.exceptions.MessageResponse;
import com.thuy.shopeeproject.service.IUserInfoService;
import com.thuy.shopeeproject.service.IUserService;
import com.thuy.shopeeproject.utils.AppUtils;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api")
public class UserAPI {
    @Autowired
    private IUserService userService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping("admin/users/get-all")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> getAllUsers(@Validated UserFilterReqDTO userFilterReqDTO, BindingResult bindingResult,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        int size = 10;
        int currentPageNumber = 0;
        if (userFilterReqDTO.getCurrentPageNumber() != null) {
            currentPageNumber = Integer.parseInt(userFilterReqDTO.getCurrentPageNumber());
        }
        pageable = PageRequest.of(currentPageNumber, size, Sort.by("username").ascending());
        Page<UserResDTO> userResDTOs = userService.findAllUser(userFilterReqDTO, pageable);
        return new ResponseEntity<>(userResDTOs, HttpStatus.OK);
    }

    @GetMapping("admin/users/info/get-all")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> getAllUserInfos() {
        List<UserInfo> userInfos = userInfoService.findAll();
        List<UserInfoResDTO> userInfoResDTOs = new ArrayList<>();
        for (UserInfo userInfo : userInfos) {
            UserInfoResDTO userResDTO = userInfo.toUserInfoResDTO();
            userInfoResDTOs.add(userResDTO);
        }
        return new ResponseEntity<>(userInfoResDTOs, HttpStatus.OK);
    }

    @GetMapping("admin/users/info")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> getUserInfoByUserId(@RequestParam("userId") Long userId) {

        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Please sign in to order");
        }
        UserInfo userInfo = optionalUser.get().getUserInfo();
        if (userInfo == null) {
            return new ResponseEntity<>("This user not have user info", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalUser.get().toUserResDTO(userInfo), HttpStatus.OK);
    }

    @GetMapping("users/info")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {

        authentication = SecurityContextHolder.getContext().getAuthentication();
        String name;
        User user = new User();

        if (authentication != null) {
            // user = authentication.getPrincipal();
            name = authentication.getName();
            user = userService.findByUsername(name).get();
            if (user.getUserInfo() == null) {
                return new ResponseEntity<>(user.toUserResDTO(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(user.toUserResDTO(user.getUserInfo()), HttpStatus.OK);
    }

    @PostMapping("users/info")
    public ResponseEntity<?> createUserInfo(Authentication authentication,
            @RequestBody @Validated UserInfoCreateReqDTO userInfoCreateReqDTO, BindingResult bindingResult) {
        if (userInfoService.existsByPhone(userInfoCreateReqDTO.getPhone())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Phone number is already taken!"));
        }

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        Optional<User> optionalUser = userService.findByUsername(name);

        if (!optionalUser.isPresent()) {
            throw new CustomErrorException(HttpStatus.FORBIDDEN, "Please login to do this");
        }

        UserInfo userInfo = userInfoService.createUserInfo(userInfoCreateReqDTO, optionalUser.get());
        return new ResponseEntity<>(userInfo.toUserInfoResDTO(), HttpStatus.CREATED);
    }

    @PatchMapping("users/info")
    public ResponseEntity<?> editUserInfo(Authentication authentication,
            @RequestBody @Validated UserInfoUpdateReqDTO userInfoUpdateReqDTO, BindingResult bindingResult) {

        UserInfoCreateReqDTO userInfoCreateReqDTO = userInfoUpdateReqDTO.toUserInfoCreateReqDTO();

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        authentication = SecurityContextHolder.getContext().getAuthentication();
        String name;

        User user = new User();

        if (authentication != null) {
            // user = authentication.getPrincipal();
            name = authentication.getName();
            user = userService.findByUsername(name).get();
            UserInfo userInfo = user.getUserInfo();

            if (userInfo == null) {
                if (userInfoService.existsByPhone(userInfoCreateReqDTO.getPhone())) {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Phone number is already taken!"));
                }
                userInfo = userInfoService.createUserInfo(userInfoUpdateReqDTO.toUserInfoCreateReqDTO(), user);
            } else {
                UserInfo newUserInfo = new UserInfo();
                Date date = new Date();

                if (userInfoCreateReqDTO.getBirthday() != null) {
                    date = appUtils.parseStringToDate(userInfoCreateReqDTO.getBirthday());
                    newUserInfo.setBirthday(date);
                }

                if (userInfoCreateReqDTO.getPhone() != null) {
                    if (userInfoService.existsByPhone(userInfoCreateReqDTO.getPhone())) {
                        return ResponseEntity
                                .badRequest()
                                .body(new MessageResponse("Phone number is already taken!"));
                    }
                }

                if (userInfoCreateReqDTO.getGender() != null) {
                    newUserInfo = userInfoCreateReqDTO.toUserInfo(user);
                } else {
                    newUserInfo = userInfoCreateReqDTO.toUserInfo(user, userInfo.getGender());
                }

                ModelMapper modelMapper = new ModelMapper();
                modelMapper.getConfiguration().setSkipNullEnabled(true);
                modelMapper.map(newUserInfo, userInfo);

                userInfo = userInfoService.save(userInfo);
            }
            return new ResponseEntity<>(userInfo.toUserInfoResDTO(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Login to action this", HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("admin/users/info")
    public ResponseEntity<?> editUserInfoByAdmin(@RequestParam("userId") Long userId,
            @RequestBody @Validated UserInfoUpdateReqDTO userInfoUpdateReqDTO, BindingResult bindingResult) {

        UserInfoCreateReqDTO userInfoCreateReqDTO = userInfoUpdateReqDTO.toUserInfoCreateReqDTO();

        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Please sign in to order");
        }

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        UserInfo userInfo = optionalUser.get().getUserInfo();
        if (userInfo == null) {
            if (userInfoService.existsByPhone(userInfoCreateReqDTO.getPhone())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Phone number is already taken!"));
            }
            userInfo = userInfoService.createUserInfo(userInfoUpdateReqDTO.toUserInfoCreateReqDTO(),
                    optionalUser.get());
        } else {
            UserInfo newUserInfo = new UserInfo();
            Date date = new Date();

            if (userInfoCreateReqDTO.getBirthday() != null) {
                date = appUtils.parseStringToDate(userInfoCreateReqDTO.getBirthday());
                newUserInfo.setBirthday(date);
            }

            if (userInfoCreateReqDTO.getPhone() != null) {
                if (userInfoService.existsByPhone(userInfoCreateReqDTO.getPhone())) {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Phone number is already taken!"));
                }
            }

            if (userInfoCreateReqDTO.getGender() != null) {
                newUserInfo = userInfoCreateReqDTO.toUserInfo(optionalUser.get());
            } else {
                newUserInfo = userInfoCreateReqDTO.toUserInfo(optionalUser.get(), userInfo.getGender());
            }

            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.map(newUserInfo, userInfo);

            userInfo = userInfoService.save(userInfo);
        }
        return new ResponseEntity<>(userInfo.toUserInfoResDTO(), HttpStatus.OK);

    }

    @DeleteMapping("admin/users/info")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> deleteUser(@RequestParam("userId") Long userId) {
        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Please sign in to order");
        }
        User user = optionalUser.get();
        userService.delete(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
