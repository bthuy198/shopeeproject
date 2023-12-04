package com.thuy.shopeeproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thuy.shopeeproject.domain.entity.ProductDetail;
import com.thuy.shopeeproject.repository.ProductDetailRepository;

@Service
@Transactional
public class ProductDetailServiceImpl implements IProductDetailService {
    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Override
    public List<ProductDetail> findAll() {
        return productDetailRepository.findAll();
    }

    @Override
    public Optional<ProductDetail> findById(Long id) {
        return productDetailRepository.findById(id);
    }

    @Override
    public ProductDetail save(ProductDetail e) {
        if (e.getQuantity() == 0) {
            e.setSold_out(true);
        }
        return productDetailRepository.save(e);
    }

    @Override
    public void delete(ProductDetail e) {
        productDetailRepository.delete(e);
    }

    @Override
    public void deleteById(Long id) {
        productDetailRepository.deleteById(id);
    }

}
