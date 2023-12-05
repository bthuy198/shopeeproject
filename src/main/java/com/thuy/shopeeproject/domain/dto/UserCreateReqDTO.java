package com.thuy.shopeeproject.domain.dto;

import java.util.Arrays;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.thuy.shopeeproject.domain.entity.User;
import com.thuy.shopeeproject.domain.entity.UserAvatar;
import com.thuy.shopeeproject.domain.enums.ERole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCreateReqDTO implements Validator {
    private Long id;

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Password must not be blank")
    private String password;

    @NotBlank(message = "Email must not be blank")
    @Pattern(regexp = "^[\\w]+@([\\w-]+\\.)+[\\w-]{2,6}$", message = "Invalid email!!!")
    private String email;
    private String role;
    private MultipartFile file;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserCreateReqDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MultipartFile file = (MultipartFile) target;
        if (file != null && !file.isEmpty()) {
            // Kiểm tra kích thước file
            if (file.getSize() > 512000) {
                String message = "File size must be less than 500 KB";
                errors.rejectValue("file", "file.size", message);
            }

            // Kiểm tra loại file
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null
                    ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                    : null;
            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");
            if (fileExtension == null || !allowedExtensions.contains(fileExtension.toLowerCase())) {
                String message = "Invalid file type. Allowed types are: jpg, jpeg, png, gif";
                errors.rejectValue("file", "file.type", message);
            }
        }
    }

    public User toUser(UserAvatar userAvatar) {
        return new User()
                .setId(id)
                .setAvatar(userAvatar)
                .setEmail(email)
                .setUsername(username)
                .setPassword(password)
                .setRole(ERole.getByValue(role));
    }

    public User toUser() {
        return new User()
                .setId(id)
                .setEmail(email)
                .setUsername(username)
                .setPassword(password)
                .setRole(ERole.getByValue(role));
    }
}
