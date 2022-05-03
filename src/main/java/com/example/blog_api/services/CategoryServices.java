package com.example.blog_api.services;

import com.example.blog_api.dtos.CategoryDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryServices {
    ResponseEntity<CategoryDTO> createCategory(CategoryDTO categoryDto, long customer_id);

    ResponseEntity<List<CategoryDTO>> getAllCategories();

    ResponseEntity<CategoryDTO> editCategory(CategoryDTO categoryDto, long category_id, long customer_id);

    ResponseEntity<CategoryDTO> deleteCategory(long category_id, long customer_id);

}
