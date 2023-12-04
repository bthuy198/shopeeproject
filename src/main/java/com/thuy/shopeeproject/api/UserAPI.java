package com.thuy.shopeeproject.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.http44.api.Response;
import com.thuy.shopeeproject.domain.dto.UserCreateReqDTO;
import com.thuy.shopeeproject.domain.entity.User;
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

        MultipartFile file = userCreateReqDTO.getFile();

        if (file != null) {
            new UserCreateReqDTO().validate(userCreateReqDTO, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        User user;

        if (file != null) {
            user = userService.createNoAvatar(userCreateReqDTO);
        } else {
            user = userService.createWithAvatar(userCreateReqDTO);
        }

        return null;
    }
}
