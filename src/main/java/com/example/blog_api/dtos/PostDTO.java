package com.example.blog_api.dtos;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class PostDTO {
    private String title;
    private String content;
}
