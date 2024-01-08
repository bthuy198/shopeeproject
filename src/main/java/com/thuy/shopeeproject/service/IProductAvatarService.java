package com.thuy.shopeeproject.service;

import java.util.List;

import com.thuy.shopeeproject.domain.entity.Product;
import com.thuy.shopeeproject.domain.entity.ProductAvatar;

public interface IProductAvatarService extends IService<ProductAvatar, String> {
    void deleteProductAvatar(Product p, ProductAvatar productAvatar);

    void deleteAllProductAvatar(Product p, List<ProductAvatar> productAvatars);
}