package com.example.blog_api.services.impl;

import com.example.blog_api.dtos.PostDTO;
import com.example.blog_api.exception.InvalidUserException;
import com.example.blog_api.exception.ResourceNotFoundException;
import com.example.blog_api.models.Category;
import com.example.blog_api.models.Comment;
import com.example.blog_api.models.User;
import com.example.blog_api.models.Post;
import com.example.blog_api.repositories.CategoryRepository;
import com.example.blog_api.repositories.CommentRepository;
import com.example.blog_api.repositories.UserRepository;
import com.example.blog_api.repositories.PostRepository;
import com.example.blog_api.services.PostServices;
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
public class PostServicesImpl implements PostServices {
    private final PostRepository postRepository;
    private final UserRepository customerRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private ModelMapper mapper;

    @Autowired
    public PostServicesImpl(PostRepository postRepository, UserRepository customerRepository, CommentRepository commentRepository, CategoryRepository categoryRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.customerRepository = customerRepository;
        this.commentRepository = commentRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }



    @Override
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostDTO> postDto =  new ArrayList<>();

        for(Post each: posts){
            postDto.add(mapper.map(each, PostDTO.class));
        }
        return ResponseEntity.ok(postDto);
    }


    @Override
    public ResponseEntity<PostDTO> getPost(long post_id) {
        Post post = postRepository.findById(post_id).get();
        return ResponseEntity.ok(mapper.map(post, PostDTO.class));
    }




    @Override
    public ResponseEntity<PostDTO> createNewPost(long customer_id, long category_id, PostDTO postDto) {
        User user = customerRepository.findById(customer_id).get();
        Category category = categoryRepository.findById(category_id).get();

        Post post = mapper.map(postDto, Post.class);

        post.setDatePosted(LocalDate.now());
        post.setTimePosted(LocalTime.now());
        post.setUser(user);
        post.setCategory(category);
        post.setNoOfLikes(0l);
        post.setNoOfComments(0l);
        postRepository.save(post);
        return new ResponseEntity<>(mapper.map(post, PostDTO.class), HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<PostDTO> editPost(long post_id, long customer_id, PostDTO postDto) {
        Post post = postRepository.findById(post_id).get();
        if (post.getUser().getId() == customer_id) {
            post = mapper.map(postDto, Post.class);
            post.setDateModified(LocalDate.now());
            post.setTimeModified(LocalTime.now());
            postRepository.save(post);

            PostDTO postDTO = mapper.map(post, PostDTO.class);
            return ResponseEntity.ok(postDTO);
        }
        throw new InvalidUserException("Sorry, you cannot edit this post");

    }



    @Override
    public ResponseEntity<PostDTO> deletePost(long post_id, long user_id) {
        Post post = postRepository.findByIdAndUserId(post_id, user_id);
        if (post == null) {
            throw new ResourceNotFoundException("Post not deleted because post does not exist or you are not authorised to delete this post");
        }
//        List<Comment> comments = commentRepository.findCommentsByPostId(post_id);
//        for(Comment each: comments){
//            commentRepository.delete(each);
//        }
        postRepository.delete(post);
        PostDTO postDTO = mapper.map(post, PostDTO.class);
        return ResponseEntity.ok(postDTO);

    }


}