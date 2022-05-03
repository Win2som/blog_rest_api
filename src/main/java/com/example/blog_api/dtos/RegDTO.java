package com.example.blog_api.dtos;

import lombok.Data;

@Data
public class RegDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
