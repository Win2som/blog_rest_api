package com.example.blog_api.controllers;

import com.example.blog_api.dtos.RegDTO;
import com.example.blog_api.models.User;
import com.example.blog_api.services.UserServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServices userServices;
    @MockBean
    private CategoryController categoryController;
    @MockBean
    private CommentController commentController;
    @MockBean
    private LikeController likeController;
    @MockBean
    private PostController postController;


    RegDTO regDTO = new RegDTO();
    User user = new User();

    @Test
    void registerNewUser() throws Exception {

    }

    @Test
    void registerAdmin() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/users/admin")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(regDTO)))
                .andExpect(status().isOk());

    }


    //another way
//    @Test
//    void registerAdmin() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        MvcResult result = mockMvc.perform(post("/users/admin")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(regDTO)))
//                .andReturn();
//        assertThat(result.getResponse().getStatus()).isEqualTo(200);
//
//    }

    @Test
    void getAllUsers() throws Exception {
//       List<RegDTO> regDTOList = List.of(new RegDTO());
//        when(userServices.getAllUsers())
//                .thenReturn((ResponseEntity<List<RegDTO>>) regDTOList);
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
//    void getAllUsers2() throws Exception {
//
////        List<RegDTO> regDTOList = List.of(new RegDTO());
////        when(userServices.getAllUsers())
////                .thenReturn((ResponseEntity<List<RegDTO>>) regDTOList);
//        RequestBuilder request = MockMvcRequestBuilders.get("/users");
//        MvcResult result = mockMvc.perform(request).andReturn();
//        assertEquals(, result.getResponse());
//    }

    @Test
    void loginUser() {
    }

    @Test
    void editUser() {
    }

    @Test
    void deleteUser() {
    }
}