package com.example.blog_api.controllers;

import com.example.blog_api.dtos.LoginDTO;
import com.example.blog_api.dtos.RegDTO;
import com.example.blog_api.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserServices userServices;

    @Autowired
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("/user")
    public ResponseEntity<RegDTO> registerNewUser(@RequestBody RegDTO regDto) {
        return userServices.registerNewUser(regDto);
    }

    @PostMapping("/admin")
    public ResponseEntity<RegDTO> registerAdmin(@RequestBody RegDTO regDto){
        return userServices.registerNewAdmin(regDto);
    }

    @GetMapping("")

    public ResponseEntity<List<RegDTO>> getAllUsers() {
        return userServices.getAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<RegDTO> loginUser(@RequestBody LoginDTO loginDto) {
        return userServices.userLogin(loginDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<RegDTO> editUser(@PathVariable long id, @RequestBody RegDTO regDto) {
        return userServices.editUser(id, regDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteUser(@PathVariable long id){
        return userServices.deleteUser(id);
    }


}
