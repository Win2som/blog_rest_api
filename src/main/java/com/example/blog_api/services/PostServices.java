package com.example.blog_api.services;

import com.example.blog_api.dtos.PostDTO;
import com.example.blog_api.models.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostServices {
    ResponseEntity<List<PostDTO>>  getAllPosts();

    ResponseEntity<PostDTO> createNewPost(long customer_id,  long category_id, PostDTO postDto);

    ResponseEntity<PostDTO> editPost(long post_id, long customer_id, PostDTO postDto);

    ResponseEntity<PostDTO> deletePost(long post_id, long customer_id);


    ResponseEntity<PostDTO> getPost(long post_id);
}
