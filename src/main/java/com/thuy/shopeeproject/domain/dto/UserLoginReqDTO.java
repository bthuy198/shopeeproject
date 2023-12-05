package com.thuy.shopeeproject.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserLoginReqDTO {

    @NotBlank(message = "Please enter username")
    private String username;

    @NotBlank(message = "Please enter password")
    private String password;
}
