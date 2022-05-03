package com.example.blog_api.services.impl;

import com.example.blog_api.dtos.CategoryDTO;
import com.example.blog_api.exception.ResourceAlreadyExistException;
import com.example.blog_api.exception.ResourceNotFoundException;
import com.example.blog_api.exception.UnauthorisedUserException;
import com.example.blog_api.models.Category;
import com.example.blog_api.models.User;
import com.example.blog_api.models.Role;
import com.example.blog_api.repositories.CategoryRepository;
import com.example.blog_api.repositories.UserRepository;
import com.example.blog_api.services.CategoryServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServicesImpl implements CategoryServices {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public CategoryServicesImpl(CategoryRepository categoryRepository, UserRepository userRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<CategoryDTO> createCategory(CategoryDTO categoryDto, long user_id) {
        Category category = categoryRepository.findByName(categoryDto.getName());
        User user = userRepository.findById(user_id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(user.getRole().equals(Role.CUSTOMER)) {
            throw new UnauthorisedUserException("You are not allowed to perform this operation");
        }
        else if(category != null){
            throw new ResourceAlreadyExistException("Category with the name "+ categoryDto.getName()+ " already exist");
        }
            category = mapper.map(categoryDto, Category.class);
            category.setUser(user);
            categoryRepository.save(category);
            CategoryDTO categoryDTO = mapper.map(category, CategoryDTO.class);

            return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);

    }


    @Override
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for(Category each: categories){
            categoryDTOS.add(mapper.map(each, CategoryDTO.class));
        }
        return ResponseEntity.ok(categoryDTOS);
    }


    @Override
    public ResponseEntity<CategoryDTO> editCategory(CategoryDTO categoryDto, long category_id, long user_id) {
        Category category = categoryRepository.findById(category_id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        User user = userRepository.findById(user_id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(user.getRole().equals(Role.CUSTOMER)) {
            throw new UnauthorisedUserException("You are not allowed to perform this operation");
        }
        if(category == null){
            throw new ResourceNotFoundException("Category with the name "+ categoryDto.getName()+ " not found");
        }
            category = mapper.map(categoryDto, Category.class);
            category.setUser(user);
            categoryRepository.save(category);
            CategoryDTO categoryDTO = mapper.map(category, CategoryDTO.class);

            return ResponseEntity.ok(categoryDTO);

    }

    @Override
    public ResponseEntity<CategoryDTO> deleteCategory(long category_id, long user_id) {
        Category category = categoryRepository.findById(category_id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        User user = userRepository.findById(user_id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if(user.getRole().equals(Role.CUSTOMER)){
            throw new UnauthorisedUserException("You are not allowed to perform this operation");
        }
        CategoryDTO categoryDTO = mapper.map(category, CategoryDTO.class);
        categoryRepository.delete(category);

        return new ResponseEntity<>(categoryDTO, HttpStatus.NO_CONTENT);
    }

}
