package com.example.blog_api.controllers;

import com.example.blog_api.dtos.CommentDTO;
import com.example.blog_api.models.Comment;
import com.example.blog_api.services.CommentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentServices commentServices;

    @Autowired
    public CommentController(CommentServices commentServices) {
        this.commentServices = commentServices;
    }



    @GetMapping("/{post_id}")
    public ResponseEntity<List<CommentDTO>> getAllPostComments(@PathVariable long post_id) {
        return commentServices.getAllCommentsToAPost(post_id);
    }

    @PostMapping("/{user_id}/{post_id}")
    public ResponseEntity<CommentDTO> commentOnPost(@RequestBody CommentDTO commentDto, @PathVariable long user_id, @PathVariable(name = "post_id") long post_id) {
        return commentServices.createNewComment(user_id,post_id,commentDto);
    }

    @PutMapping("/{user_id}/{comment_id}")
    public ResponseEntity<CommentDTO> editComment(@RequestBody CommentDTO commentDto, @PathVariable long user_id, @PathVariable long comment_id) {
        return commentServices.editComment(comment_id, user_id, commentDto);
    }

    @DeleteMapping("/{user_id}/{comment_id}")
    public ResponseEntity<Long> deleteComment(@PathVariable long user_id, @PathVariable long comment_id) {
        return commentServices.deleteComment(comment_id, user_id);
    }
}
