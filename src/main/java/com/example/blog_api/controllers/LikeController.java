package com.example.blog_api.controllers;

import com.example.blog_api.services.LikeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/likes")
@RestController
public class LikeController {
    private final LikeServices likeServices;

    @Autowired
    public LikeController(LikeServices likeServices) {
        this.likeServices = likeServices;
    }


    @PostMapping("posts/{post_id}/{user_id}")
    public ResponseEntity<Long> likePost(@PathVariable long post_id, @PathVariable long user_id) {

        return likeServices.likePost(post_id, user_id);
    }


    @DeleteMapping("posts/{post_id}/{user_id}")
    public ResponseEntity<Long> unLikePost(@PathVariable long post_id, @PathVariable long user_id) {

        return likeServices.unLikePost(post_id, user_id);
    }


    @PostMapping("comments/{comment_id}/{user_id}")
    public ResponseEntity<Long> likeComment(@PathVariable long comment_id, @PathVariable long user_id) {
        return likeServices.likeComment(comment_id, user_id);

    }


    @DeleteMapping("comments/{comment_id}/{user_id}")
    public ResponseEntity<Long> unLikeComment(@PathVariable long comment_id, @PathVariable long user_id) {
        return likeServices.unLikeComment(comment_id, user_id);

    }


}