package com.thuy.shopeeproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.thuy.shopeeproject.domain.entity.Product;
import com.thuy.shopeeproject.domain.entity.ProductAvatar;
import com.thuy.shopeeproject.exceptions.CustomErrorException;
import com.thuy.shopeeproject.repository.ProductAvatarRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductAvatarServiceImpl implements IProductAvatarService {

    @Autowired
    private ProductAvatarRepository productAvatarRepository;

    @Value("${application.cloudinary.server-name}")
    private String cloudinaryServerName;

    @Value("${application.cloudinary.default-folder}/product")
    private String cloudinaryDefaultFolderProduct;

    @Value("${application.cloudinary.default-file-name}")
    private String cloudinaryDefaultFileName;

    @Value("${application.cloudinary.default-image-url}")
    private String cloudinaryDefaultImage;

    @Override
    public List<ProductAvatar> findAll() {
        return productAvatarRepository.findAll();
    }

    @Override
    public ProductAvatar save(ProductAvatar e) {
        return productAvatarRepository.save(e);
    }

    @Override
    public void delete(ProductAvatar e) {
        productAvatarRepository.delete(e);
    }

    @Override
    public Optional<ProductAvatar> findById(String id) {
        return productAvatarRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        productAvatarRepository.deleteById(id);
    }

    @Override
    public void deleteProductAvatar(Product p, ProductAvatar productAvatar) {
        String fileName = productAvatar.getFileName();
        String nullFileName = "null_" + productAvatar.getProduct().getId();
        if (fileName.contains(nullFileName)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "This product don't have image to delete");
        }
        if (productAvatar.getIsDefault() == true) {
            productAvatarRepository.delete(productAvatar);
            List<ProductAvatar> avatars = p.getProductAvatars();
            ProductAvatar avatar1 = avatars.get(0);
            avatar1.setIsDefault(true);
            avatar1 = productAvatarRepository.save(avatar1);
        } else {
            productAvatarRepository.delete(productAvatar);
        }
    }

    @Override
    public void deleteAllProductAvatar(Product product, List<ProductAvatar> productAvatars) {
        for (ProductAvatar avatar : productAvatars) {
            productAvatarRepository.delete(avatar);
        }
        ProductAvatar productAvatar = new ProductAvatar();
        productAvatar.setFileFolder(cloudinaryDefaultFolderProduct);
        productAvatar.setFileUrl(cloudinaryDefaultImage);
        productAvatar.setProduct(product);
        productAvatar.setCloudId("shopee_project/product/" + productAvatar.getCloudId());
        productAvatar = productAvatarRepository.save(productAvatar);
        productAvatar.setFileName("null_" + product.getId() + ".jpg");
        productAvatar = productAvatarRepository.save(productAvatar);
    }
}
