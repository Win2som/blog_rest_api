package com.example.blog_api.services.impl;

import com.example.blog_api.dtos.CategoryDTO;
import com.example.blog_api.exception.ResourceAlreadyExistException;
import com.example.blog_api.exception.UnauthorisedUserException;
import com.example.blog_api.models.Category;
import com.example.blog_api.models.Role;
import com.example.blog_api.models.User;
import com.example.blog_api.repositories.CategoryRepository;
import com.example.blog_api.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServicesImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    ModelMapper mapper;

    @InjectMocks
    private CategoryServicesImpl underTest;

    private Category category;
    private CategoryDTO categoryDTO;
    private User user;

    @BeforeEach
    void setUp(){
        categoryDTO = new CategoryDTO();
        categoryDTO.setName("kids");
        categoryDTO.setDescription("materials from turkey");

        category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        user = new User();
        user.setRole(Role.ADMIN);
    }


    @Test
    void adminCanCreateCategory() {
        when(categoryRepository.findByName(categoryDTO.getName())).thenReturn(null);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(mapper.map(categoryDTO, Category.class)).thenReturn(category);
        when(mapper.map(category, CategoryDTO.class)).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = underTest.createCategory(categoryDTO, user.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(categoryDTO.getName());
    }

    @Test
    void getAllCategories() {
        List<Category> categoryList = List.of(category);
        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(mapper.map(category, CategoryDTO.class)).thenReturn(categoryDTO);

        ResponseEntity<List<CategoryDTO>> response = underTest.getAllCategories();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(categoryList.size());
    }

    @Test
    void adminCanEditCategory() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.ofNullable(category));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(mapper.map(categoryDTO, Category.class)).thenReturn(category);
        when(mapper.map(category, CategoryDTO.class)).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = underTest.editCategory(categoryDTO, category.getId(), user.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(categoryDTO);
    }

    @Test
    void adminCanDeleteCategory() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.ofNullable(category));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(mapper.map(category, CategoryDTO.class)).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = underTest.deleteCategory(category.getId(), user.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isEqualTo(categoryDTO);
    }

    @Test
    void canThrowExceptionsWhenCustomerTriesToPerformCreateUpdateAndDeleteCategoryOperations(){
        User user1 = new User();
        user1.setRole(Role.CUSTOMER);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.ofNullable(category));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user1));

        assertThatThrownBy(() -> underTest.createCategory(categoryDTO, user.getId()))
                .isInstanceOf(UnauthorisedUserException.class)
                .hasMessageContaining("You are not allowed to perform this operation");

        assertThatThrownBy(() -> underTest.editCategory(categoryDTO, user.getId(), category.getId()))
                .isInstanceOf(UnauthorisedUserException.class)
                .hasMessageContaining("You are not allowed to perform this operation");

        assertThatThrownBy(() -> underTest.deleteCategory(category.getId(), user.getId()))
                .isInstanceOf(UnauthorisedUserException.class)
                .hasMessageContaining("You are not allowed to perform this operation");

    }

    @Test
    void canThrowExceptionsWhenCategoryAlreadyExist(){

        when(categoryRepository.findByName(categoryDTO.getName())).thenReturn(category);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        assertThatThrownBy(() -> underTest.createCategory(categoryDTO, user.getId()))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessageContaining("Category with the name "+ categoryDTO.getName()+ " already exist");

    }
}