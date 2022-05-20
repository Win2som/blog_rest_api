package com.example.blog_api.controllers;

import com.example.blog_api.dtos.CategoryDTO;
import com.example.blog_api.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryServices categoryServices;

    @Autowired
    public CategoryController(CategoryServices categoryServices) {

        this.categoryServices = categoryServices;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{user_id}")
    public ResponseEntity<CategoryDTO> createNewCategory(@RequestBody CategoryDTO categoryDto, @PathVariable long user_id){
        return categoryServices.createCategory(categoryDto, user_id);
    }


    @GetMapping("/")
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        return categoryServices.getAllCategories();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{user_id}/{category_id}")
    public ResponseEntity<CategoryDTO> updateExistingCategory(@RequestBody CategoryDTO categoryDto, @PathVariable long category_id, @PathVariable long user_id){
        return categoryServices.editCategory(categoryDto, category_id, user_id);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{user_id}/{category_id}")
    public ResponseEntity<CategoryDTO> deleteExistingCategory(@PathVariable long category_id, @PathVariable long user_id){
        return categoryServices.deleteCategory(category_id, user_id);
    }

}
