package com.example.blog_api.controllers;

import com.example.blog_api.dtos.PostDTO;
import com.example.blog_api.models.Post;
import com.example.blog_api.services.PostServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;



    @RestController
    @RequestMapping("/posts")
    public class PostController {
        private final PostServices postServices;

        @Autowired
        public PostController(PostServices postServices) {
            this.postServices = postServices;
        }


        @GetMapping("")
        public ResponseEntity<List<PostDTO>>  getAllPosts() {
            return postServices.getAllPosts();
        }

        @GetMapping("/{post_id}")
        public ResponseEntity<PostDTO> getSinglePost(@PathVariable long post_id) {
            return postServices.getPost(post_id);
        }



        @PostMapping("/{user_Id}/{category_id}")
        public ResponseEntity<PostDTO> addNewPost(@RequestBody PostDTO postDTO, @PathVariable long user_Id, @PathVariable long category_id){
            return postServices.createNewPost(user_Id, category_id,postDTO);
        }


        @PutMapping("/{user_id}/{post_id}")
        public ResponseEntity<PostDTO> editPost(@RequestBody PostDTO postDto,@PathVariable long user_id,
                                             @PathVariable long post_id ) {
            return postServices.editPost(post_id, user_id, postDto);
        }


        @DeleteMapping("/{user_id}/{post_id}")
        public ResponseEntity<PostDTO> deletePost(@PathVariable long user_id, @PathVariable long post_id) {
            return postServices.deletePost(post_id, user_id);
        }


}
