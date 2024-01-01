package com.thuy.shopeeproject.domain.dto.product;

import com.thuy.shopeeproject.domain.entity.ProductAvatar;
import com.thuy.shopeeproject.domain.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductAvatarDTO {
    private String id;
    private String fileName;
    private String fileFolder;
    private String fileUrl;
    private String fileType;
    private String cloudId;
    private Integer width;
    private Integer height;
    private Boolean isDefault;

    public ProductAvatar toAvatar(Product product) {
        return new ProductAvatar()
                .setId(id)
                .setFileName(fileName)
                .setFileFolder(fileFolder)
                .setFileUrl(fileUrl)
                .setFileType(fileType)
                .setCloudId(cloudId)
                .setHeight(height)
                .setWidth(width)
                .setProduct(product);
    }
}
