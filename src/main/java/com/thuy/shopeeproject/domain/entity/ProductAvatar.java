package com.thuy.shopeeproject.domain.entity;

import jakarta.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.thuy.shopeeproject.domain.dto.product.ProductAvatarDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_avatars")
public class ProductAvatar {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_folder")
    private String fileFolder;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "cloud_id")
    private String cloudId;

    private Integer width;
    private Integer height;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    public ProductAvatarDTO toProductAvatarDTO() {
        return new ProductAvatarDTO()
                .setId(id)
                .setCloudId(cloudId)
                .setFileFolder(fileFolder)
                .setFileName(fileName)
                .setFileType(fileType)
                .setFileUrl(fileUrl)
                .setIsDefault(isDefault);
    }
}
