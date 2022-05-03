package com.example.blog_api.services;

import com.example.blog_api.dtos.CommentDTO;
import com.example.blog_api.models.Comment;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface CommentServices {
    ResponseEntity<List<CommentDTO>>  getAllCommentsToAPost(long pid);


    ResponseEntity<CommentDTO> createNewComment(long customer_id, long post_id, CommentDTO postDto);

    ResponseEntity<Long> deleteComment(long comment_id, long customer_id);

    ResponseEntity<CommentDTO> editComment(long comment_id, long customer_id, CommentDTO postDto);
}
