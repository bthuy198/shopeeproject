package com.thuy.shopeeproject.domain.dto.product;

import java.util.Arrays;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.thuy.shopeeproject.domain.entity.Category;
import com.thuy.shopeeproject.domain.entity.Product;

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
public class ProductUpdateReqDTO implements Validator {

    // @NotBlank(message = "Product name must not be blank")
    private String productName;

    private Long categoryId;

    private String description;

    private MultipartFile[] files;

    public Product toProduct(Category category) {
        return new Product()
                .setProductName(productName)
                .setCategory(category)
                .setDescription(description);
    }

    public Product toProduct(Product product, Category category) {
        return new Product()
                .setProductName(productName)
                .setCategory(category)
                .setDescription(description)
                .setProductAvatars(product.getProductAvatars())
                .setProductDetails(product.getProductDetails());
    }

    public Product toProduct(Product product) {
        return new Product()
                .setProductName(productName)
                .setDescription(description)
                .setProductAvatars(product.getProductAvatars())
                .setProductDetails(product.getProductDetails());
    }

    public Product toProductUpdateNoAvatar(Category category) {
        return new Product()
                .setCategory(category)
                .setDescription(description)
                .setProductName(productName);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductCreateReqDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MultipartFile[] files = (MultipartFile[]) target;

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (file != null && !file.isEmpty()) {
                // Kiểm tra kích thước file
                if (file.getSize() > 512000) {
                    String message = "File size at index " + i + " must be less than 500 KB";
                    errors.rejectValue("files", "file.size", message);
                }

                // Kiểm tra loại file
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename != null
                        ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                        : null;
                List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");
                if (fileExtension == null || !allowedExtensions.contains(fileExtension.toLowerCase())) {
                    String message = "Invalid file type at index " + i + ". Allowed types are: jpg, jpeg, png, gif";
                    errors.rejectValue("files", "file.type", message);
                }
            }
        }
    }
}
