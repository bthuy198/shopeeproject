package com.thuy.shopeeproject.domain.dto;

import jakarta.validation.constraints.NotBlank;

import com.thuy.shopeeproject.domain.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDTO {
    private Long id;

    @NotBlank(message = "Category name must be not blank")
    private String categoryName;

    public Category toCategory(){
        Category category = new Category();
		category.setId(id);
		category.setCategoryName(categoryName);
		return category;
    };
}
