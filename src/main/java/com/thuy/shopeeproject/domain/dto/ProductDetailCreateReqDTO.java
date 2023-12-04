package com.thuy.shopeeproject.domain.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

import com.thuy.shopeeproject.domain.entity.Product;
import com.thuy.shopeeproject.domain.entity.ProductDetail;
import com.thuy.shopeeproject.domain.entity.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDetailCreateReqDTO {
    private Long id;

    @NotBlank(message = "Size must be not blank")
    private String size_str;

    @Pattern(regexp = "^[1-9]\\d*$", message = "Price is not valid")
    @Pattern(regexp = "^(?!0)\\d{5,6}$", message = "Price must be between 10,000 VNĐ and 1,000,000 VNĐ")
    @NotBlank(message = "Price must be not blank")
    private String price;

    @Pattern(regexp = "^(0|[1-9]\\d*)$", message = "Quantity is not valid")
    @NotBlank(message = "Quantity must be not blank")
    private String quantity;

    public ProductDetail toProductDetail(Size size, Product product) {
        return new ProductDetail()
                .setId(id)
                .setPrice(BigDecimal.valueOf(Long.parseLong(price)))
                .setQuantity(Long.parseLong(quantity))
                .setSize(size)
                .setProduct(product);
    }
}
