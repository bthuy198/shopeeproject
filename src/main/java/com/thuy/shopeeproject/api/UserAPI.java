package com.thuy.shopeeproject.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thuy.shopeeproject.domain.dto.UserCreateReqDTO;
import com.thuy.shopeeproject.domain.entity.User;
import com.thuy.shopeeproject.domain.enums.ERole;
import com.thuy.shopeeproject.exceptions.CustomErrorException;
import com.thuy.shopeeproject.service.IUserService;
import com.thuy.shopeeproject.utils.AppUtils;

@RestController
@RequestMapping("api/users")
public class UserAPI {
    @Autowired
    private IUserService userService;

    @Autowired
    private AppUtils appUtils;

    @PostMapping("")
    public ResponseEntity<?> createUser(UserCreateReqDTO userCreateReqDTO, BindingResult bindingResult) {

        String role = userCreateReqDTO.getRole();

        if (!isRoleValid(role)) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Not found this user's role");
        }

        MultipartFile file = userCreateReqDTO.getFile();

        if (file != null) {
            new UserCreateReqDTO().validate(userCreateReqDTO, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        User user = userService.save(userCreateReqDTO.toUser());
        ;

        if (file != null) {
            user = userService.createWithAvatar(userCreateReqDTO, user);
        } else {
            user = userService.createNoAvatar(userCreateReqDTO, user);
        }

        userService.save(user);

        return new ResponseEntity<>(user.toUserCreateResDTO(), HttpStatus.CREATED);
    }

    public boolean isRoleValid(String role) {
        for (ERole eRole : ERole.values()) {
            if (eRole.getValue().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
