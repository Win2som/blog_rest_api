package com.example.blog_api.services.impl;

import com.example.blog_api.dtos.CommentDTO;
import com.example.blog_api.exception.InvalidUserException;
import com.example.blog_api.exception.ResourceNotFoundException;
import com.example.blog_api.models.Comment;
import com.example.blog_api.models.User;
import com.example.blog_api.models.Post;
import com.example.blog_api.repositories.CommentRepository;
import com.example.blog_api.repositories.UserRepository;
import com.example.blog_api.repositories.PostRepository;
import com.example.blog_api.services.CommentServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServicesImpl implements CommentServices{
        private final UserRepository userRepository;
        private final PostRepository postRepository;
        private final CommentRepository commentRepository;
        private final ModelMapper mapper;

        @Autowired
        public CommentServicesImpl(UserRepository userRepository, PostRepository postRepository, ModelMapper mapper, CommentRepository commentRepository) {
            this.userRepository = userRepository;
            this.postRepository = postRepository;
            this.commentRepository = commentRepository;
            this.mapper = mapper;
        }

        @Override
        public ResponseEntity<List<CommentDTO>> getAllCommentsToAPost(long post_id) {
            List<Comment> comments = commentRepository.findCommentsByPostId(post_id);
            List<CommentDTO> commentDtos = new ArrayList<>();
            for(Comment e: comments){
                commentDtos.add(mapper.map(e,CommentDTO.class));
            }
            return ResponseEntity.ok(commentDtos);
        }



    @Override
        public ResponseEntity<CommentDTO> createNewComment(long customer_id, long post_id, CommentDTO commentDto) {
            Comment comment;
            User user = userRepository.getById(customer_id);
            Post post = postRepository.getById(post_id);
            comment = mapper.map(commentDto, Comment.class);
            comment.setDateCommented(LocalDate.now());
            comment.setTimeCommented(LocalTime.now());
            comment.setPost(post);
            comment.setUser(user);
            comment.setNoOflikes(0l);
            commentRepository.save(comment);
            post.setNoOfComments(commentRepository.findCommentsByPostId(post_id).size());
            postRepository.save(post);
            CommentDTO cdto = mapper.map(comment,CommentDTO.class);

            return new ResponseEntity<>(cdto, HttpStatus.CREATED);
        }

        @Override
        public ResponseEntity<CommentDTO> editComment(long comment_id, long customer_id, CommentDTO commentDto) {
            Comment comment = commentRepository.findById(comment_id).get();
            if (comment.getUser().getId() == customer_id) {
                comment = mapper.map(commentDto, Comment.class);
                commentRepository.save(comment);
                CommentDTO commentDTO = mapper.map(comment, CommentDTO.class);
                return ResponseEntity.ok(commentDTO);
            }
            throw new InvalidUserException("Sorry, you cannot edit this comment");
        }


        public ResponseEntity<Long> deleteComment(long comment_id, long customer_id) {
            Comment comment =  commentRepository.findById(comment_id).get();
            if (comment.getUser().getId() == customer_id) {
                comment.getPost().setNoOfComments(comment.getPost().getNoOfComments() - 1);
                postRepository.save(comment.getPost());
                commentRepository.delete(comment);
                return  new ResponseEntity<>(comment_id, HttpStatus.NO_CONTENT);
            }
            throw new ResourceNotFoundException("Comment not found");
        }

}
