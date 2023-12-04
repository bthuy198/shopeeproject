package com.thuy.shopeeproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuy.shopeeproject.domain.entity.ProductAvatar;
import com.thuy.shopeeproject.repository.ProductAvatarRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductAvatarServiceImpl implements IProductAvatarService {

    @Autowired
    private ProductAvatarRepository productAvatarRepository;

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
}
