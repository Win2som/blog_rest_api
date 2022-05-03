package com.example.blog_api.services;

import com.example.blog_api.dtos.LoginDTO;
import com.example.blog_api.dtos.RegDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserServices {


    ResponseEntity<RegDTO>  registerNewUser(RegDTO regDto);

    ResponseEntity<RegDTO> userLogin(LoginDTO loginDTO);

    ResponseEntity<List<RegDTO>> getAllUsers();

    ResponseEntity<RegDTO> editUser(long id, RegDTO regDto);

    ResponseEntity<Long> deleteUser(long id);

    ResponseEntity<RegDTO> registerNewAdmin(RegDTO regDto);
}
