package com.thuy.shopeeproject.domain.dto.user;

import com.thuy.shopeeproject.domain.dto.product.ProductFilterReqDTO;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserFilterReqDTO {
    @Pattern(regexp = "^[0-9]+$", message = "Invalid page number")
    private String currentPageNumber;
    private String keyWord;
    private String role;
}
