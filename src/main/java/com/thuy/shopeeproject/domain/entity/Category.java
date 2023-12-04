package com.thuy.shopeeproject.domain.entity;

import jakarta.persistence.*;

import com.thuy.shopeeproject.domain.dto.CategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="categories")
public class Category extends BaseEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="category_name")
	private String categoryName;

	public CategoryDTO toCategoryDTO() {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setId(id);
		categoryDTO.setCategoryName(categoryName);
		return categoryDTO;
	}
}
