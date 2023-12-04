package com.thuy.shopeeproject.service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thuy.shopeeproject.domain.entity.Category;
import com.thuy.shopeeproject.repository.CategoryRepository;

@Service
@Transactional
public class CategoryServiceImpl implements ICategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category save(Category e) {
        return categoryRepository.save(e);
    }

    @Override
    public void delete(Category e) {
        categoryRepository.delete(e);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
    
}
