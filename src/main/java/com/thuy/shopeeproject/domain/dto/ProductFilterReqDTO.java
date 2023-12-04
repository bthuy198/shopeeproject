package com.thuy.shopeeproject.domain.dto;

import java.math.BigDecimal;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductFilterReqDTO implements Validator {
    private Integer currentPageNumber;
    private String keyWord;
    private Integer categoryId;

    @Pattern(regexp = "^(0|[1-9]\\d*)$", message = "Invalid price!!")
    private String minPrice;

    @Pattern(regexp = "^10000$|^[1-9]\\d{4,}$", message = "Invalid price!!")
    private String maxPrice;

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductFilterReqDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductFilterReqDTO productFilterReqDTO = (ProductFilterReqDTO) target;
        String min = productFilterReqDTO.getMinPrice();
        String max = productFilterReqDTO.getMaxPrice();

        if (min != null && max != null) {
            BigDecimal minPrice = BigDecimal.valueOf(Long.parseLong(productFilterReqDTO.getMinPrice()));
            BigDecimal maxPrice = BigDecimal.valueOf(Long.parseLong(productFilterReqDTO.getMaxPrice()));
            if (minPrice.compareTo(maxPrice) == 1) {
                errors.rejectValue("maxPrice", "maxPrice.value", "Invalid price range");
                errors.rejectValue("minPrice", "minPrice.value", "Invalid price range");
                return;
            }
        }
    }
}
