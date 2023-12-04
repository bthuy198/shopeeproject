package com.thuy.shopeeproject.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuy.shopeeproject.domain.entity.Category;
import com.thuy.shopeeproject.exceptions.CustomErrorException;
import com.thuy.shopeeproject.service.CategoryServiceImpl;

@RestController
@RequestMapping("/api/categories")
public class CategoryAPI {
    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping()
    public ResponseEntity<?> getAllCategory(){
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id){
        Optional<Category> category = categoryService.findById(id);
        if(category.isPresent()){
            return ResponseEntity.ok().body(category.get());
        }
        throw new CustomErrorException(HttpStatus.NOT_FOUND, "Category not found");
    }
}
