package com.thuy.shopeeproject.service;

import com.thuy.shopeeproject.domain.entity.ProductDetail;

public interface IProductDetailService extends IService<ProductDetail, Long> {
    ProductDetail findByProductIdAndSize(Long productId, Long sizeId);

    ProductDetail updateProductDetail(ProductDetail productDetail);
}
