package com.example.blog_api.services.impl;

import com.example.blog_api.dtos.LoginDTO;
import com.example.blog_api.dtos.RegDTO;
import com.example.blog_api.exception.InvalidUserException;
import com.example.blog_api.exception.ResourceAlreadyExistException;
import com.example.blog_api.models.User;
import com.example.blog_api.models.Role;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServicesImplTest {

    @Mock
    private UserRepository customerRepository;

    @InjectMocks
    private UserServicesImpl underTest;

    @Mock
    private ModelMapper mapper;

    private User user;

    private RegDTO regDTO;

    @BeforeEach
    void setUp(){
        regDTO = new RegDTO();
        regDTO.setFirstName("Chisom");
        regDTO.setLastName("Obidike");
        regDTO.setEmail("obidike@yahoo.com");
        regDTO.setPassword("1234");


        user = new User();
        user.setFirstName(regDTO.getFirstName());
        user.setLastName(regDTO.getLastName());
        user.setEmail(regDTO.getEmail());
        user.setPassword(regDTO.getPassword());
        user.setDateRegistered(LocalDate.now());

    }



    @Test
    void canGetAllCustomers() {

        List<User> userList = List.of(new User(2, "chisom","obidike", "obid@yahoo.com", "1234", Role.CUSTOMER, LocalDate.now()));
        when(customerRepository.findAll()).thenReturn(userList);
        ResponseEntity<List<RegDTO>> response = underTest.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(userList.size());
    }


    @Test
    void canRegisterNewCustomer() {

        when(customerRepository.findByEmail(regDTO.getEmail())).thenReturn(null);

        when(mapper.map(regDTO, User.class)).thenReturn(user);
        when(mapper.map(user, RegDTO.class)).thenReturn(regDTO);

        ResponseEntity<RegDTO> response = underTest.registerNewUser(regDTO);

        assertThat(response.getBody()).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody().getEmail()).isEqualTo(user.getEmail());

    }


    @Test
    void canRegisterNewAdmin() {

        when(customerRepository.findByEmail(regDTO.getEmail())).thenReturn(null);

        when(mapper.map(regDTO, User.class)).thenReturn(user);
        when(mapper.map(user, RegDTO.class)).thenReturn(regDTO);

        ResponseEntity<RegDTO> response = underTest.registerNewAdmin(regDTO);

        assertThat(response.getBody()).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody().getEmail()).isEqualTo(user.getEmail());

    }

    @Test
    void willThrowWhenEmailIsTaken(){
//        RegDTO regDTO = new RegDTO();
//        regDTO.setFirstName("Chisom");
//        regDTO.setLastName("Obidike");
//        regDTO.setEmail("obidike@yahoo.com");
//        regDTO.setPassword("1234");


        given(customerRepository.findByEmail(regDTO.getEmail()))
                .willReturn(new User());

        assertThatThrownBy(() -> underTest.registerNewUser(regDTO))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessageContaining("This email "+regDTO.getEmail()+" has been taken");

        assertThatThrownBy(() -> underTest.registerNewAdmin(regDTO))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessageContaining("This email "+regDTO.getEmail()+" has been taken");
    }

    @Test
    void customerCanLogin() {
        LoginDTO loginDto = new LoginDTO();
        loginDto.setEmail("obidike@yahoo.com");
        loginDto.setPassword("1234");

        when(customerRepository.findByEmail(loginDto.getEmail())).thenReturn(user);
        when(mapper.map(user, RegDTO.class)).thenReturn(regDTO);

        ResponseEntity<RegDTO> response = underTest.userLogin(loginDto);

        assertThat(response.getBody().getEmail()).isEqualTo(loginDto.getEmail());
        assertThat(response.getBody().getPassword()).isEqualTo(loginDto.getPassword());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void willThrowExceptionWhenPasswordIsIncorrect(){
        LoginDTO loginDto = new LoginDTO();
        loginDto.setEmail("obidike@yahoo.com");
        loginDto.setPassword("12344");

        when(customerRepository.findByEmail(loginDto.getEmail())).thenReturn(user);
//        when(mapper.map(customer, RegDTO.class)).thenReturn(regDTO);

        assertThatThrownBy(() -> underTest.userLogin(loginDto))
                .isInstanceOf(InvalidUserException.class)
                .hasMessageContaining("Could not process. Please check your email and password and try again");
    }

    @Test
    void canEditCustomerDetails() {
        when(customerRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(mapper.map(regDTO, User.class)).thenReturn(user);
        when(mapper.map(user, RegDTO.class)).thenReturn(regDTO);

        ResponseEntity<RegDTO> response = underTest.editUser(user.getId(),regDTO);

        assertThat(response.getBody().getEmail()).isEqualTo(regDTO.getEmail());
    }


    @Test
    void CanDeleteCustomer() {

        ResponseEntity<Long> response = underTest.deleteUser(user.getId());

        assertThat(response.getBody()).isEqualTo(user.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}