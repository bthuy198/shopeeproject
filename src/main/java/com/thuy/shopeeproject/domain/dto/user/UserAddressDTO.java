package com.thuy.shopeeproject.domain.dto.user;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAddressDTO {
    private String name;
    private String phone;
    private String address;
}
