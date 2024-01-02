package com.thuy.shopeeproject.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.thuy.shopeeproject.domain.dto.product.ProductCreateReqDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductFilterReqDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductResDTO;
import com.thuy.shopeeproject.domain.dto.product.ProductUpdateReqDTO;
import com.thuy.shopeeproject.domain.entity.Category;
import com.thuy.shopeeproject.domain.entity.Product;

public interface IProductService extends IService<Product, Long> {

    Page<ProductResDTO> findAll(ProductFilterReqDTO productFilterReqDTO, Pageable pageable);

    Product createProduct(ProductCreateReqDTO productCreateReqDTO, Category category);

    Product createProductWithAvatars(ProductCreateReqDTO productCreateReqDTO, Category category);

    Product updateProductNoAvatar(ProductUpdateReqDTO productUpdateReqDTO, Product product, Category category);

    Product updateProductWithAvatar(ProductUpdateReqDTO productUpdateReqDTO, Product product);

}
