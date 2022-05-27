package com.example.blog_api.services.impl;

import com.example.blog_api.dtos.LoginDTO;
import com.example.blog_api.dtos.RegDTO;
import com.example.blog_api.exception.ResourceAlreadyExistException;
import com.example.blog_api.exception.InvalidUserException;
import com.example.blog_api.exception.ResourceNotFoundException;
import com.example.blog_api.models.User;
import com.example.blog_api.models.Role;
import com.example.blog_api.repositories.UserRepository;
import com.example.blog_api.services.UserServices;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServicesImpl implements UserServices {

    private final UserRepository userRepository;
    private ModelMapper mapper;


    public UserServicesImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<List<RegDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<RegDTO> userDto =  new ArrayList<>();

        for(User each: users){
            userDto.add(mapper.map(each, RegDTO.class));
        }
        return ResponseEntity.ok(userDto);
    }


    @Override
    public ResponseEntity<RegDTO> registerNewUser(RegDTO regDto) {
        User user = userRepository.findByEmail(regDto.getEmail());

        if(user == null){
            user = mapper.map(regDto, User.class);
            user.setDateRegistered(LocalDate.now());
            user.setRole(Role.CUSTOMER);
            user.setPassword(user.getPassword());

            userRepository.save(user);


            RegDTO regDTO = mapper.map(user, RegDTO.class);
            return new ResponseEntity<>(regDTO, HttpStatus.CREATED);
        }

        throw new ResourceAlreadyExistException("This email "+regDto.getEmail()+" has been taken");
    }

    @Override
    public ResponseEntity<RegDTO> registerNewAdmin(RegDTO regDto) {
        User admin = userRepository.findByEmail(regDto.getEmail());

        if(admin == null){
            admin = mapper.map(regDto, User.class);
            admin.setDateRegistered(LocalDate.now());
            admin.setRole(Role.ADMIN);
            admin.setPassword(admin.getPassword());


            userRepository.save(admin);

            RegDTO regDTO = mapper.map(admin, RegDTO.class);
            return new ResponseEntity<>(regDTO, HttpStatus.CREATED);
        }
        throw new ResourceAlreadyExistException("This email "+regDto.getEmail()+" has been taken");
    }


    @Override
    public ResponseEntity<RegDTO> userLogin(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());

        if(user == null || !user.getPassword().equals(loginDTO.getPassword())) {
            throw new InvalidUserException("Could not process. Please check your email and password and try again");
        }

        return ResponseEntity.ok(mapper.map(user, RegDTO.class));
    }


    @Override
    public ResponseEntity<RegDTO> editUser(long id, RegDTO regDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with Id "+id+" does not exist"));
        if(user != null) {
            user.setPassword(regDto.getPassword());
            user.setEmail(regDto.getEmail());
            user.setLastName(regDto.getLastName());
            user.setFirstName(regDto.getFirstName());
            userRepository.save(user);
            return ResponseEntity.ok(mapper.map(user, RegDTO.class));
        }
        throw new ResourceNotFoundException("The User with id: "+id+" does not exist");
    }


    @Override
    public ResponseEntity<Long> deleteUser(long id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.NO_CONTENT);
    }




}
