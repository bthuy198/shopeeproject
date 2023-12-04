package com.thuy.shopeeproject.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.thuy.shopeeproject.domain.dto.ProductCreateReqDTO;
import com.thuy.shopeeproject.domain.dto.ProductFilterReqDTO;
import com.thuy.shopeeproject.domain.dto.ProductResDTO;
import com.thuy.shopeeproject.domain.dto.ProductUpdateReqDTO;
import com.thuy.shopeeproject.domain.entity.Category;
import com.thuy.shopeeproject.domain.entity.Product;

public interface IProductService extends IService<Product, Long> {

    Page<ProductResDTO> findAll(ProductFilterReqDTO productFilterReqDTO, Pageable pageable);

    Product createProduct(ProductCreateReqDTO productCreateReqDTO, Category category);

    Product createProductWithAvatars(ProductCreateReqDTO productCreateReqDTO, Category category);

    Product updateProductNoAvatar(ProductUpdateReqDTO productUpdateReqDTO, Product product, Category category);

    Product updateProductWithAvatar(ProductUpdateReqDTO productUpdateReqDTO, Product product);
}
