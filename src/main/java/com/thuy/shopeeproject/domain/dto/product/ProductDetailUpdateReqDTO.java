package com.thuy.shopeeproject.domain.dto.product;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDetailUpdateReqDTO {
    // @Pattern(regexp = "^[1-9]\\d*$", message = "Price is not valid")
    @Pattern(regexp = "^(1000000|([1-9]\\d{4,5}))$", message = "Price must be between 10,000 VNĐ and 1,000,000 VNĐ")
    private String price;

    @Pattern(regexp = "^(0|[1-9]\\d*)$", message = "Quantity is not valid")
    // @NotBlank(message = "Quantity must be not blank")
    private String quantity;
}
