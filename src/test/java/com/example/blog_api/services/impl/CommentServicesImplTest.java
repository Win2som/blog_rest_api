package com.example.blog_api.services.impl;

import com.example.blog_api.dtos.CommentDTO;
import com.example.blog_api.models.Comment;
import com.example.blog_api.models.Post;
import com.example.blog_api.models.User;
import com.example.blog_api.repositories.CommentRepository;
import com.example.blog_api.repositories.PostRepository;
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

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServicesImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    ModelMapper mapper;

    @InjectMocks
    private CommentServicesImpl underTest;

    private Comment comment;
    private CommentDTO commentDTO;
    private User user;
    private Post post;

    @BeforeEach
    void setUp(){
        commentDTO = new CommentDTO();
        commentDTO.setContent("i love it");

        comment = new Comment();
        comment.setContent(commentDTO.getContent());

        user = new User();
        user.setFirstName("tom");
        user.setLastName("timothy");
        user.setEmail("timtom@gmail.com");
        user.setPassword("1234");

        post = new Post();
        post.setTitle("sun shades");
        post.setContent("affordable shades");


    }

    @Test
    void getAllCommentsToAPost() {
        List<Comment> commentList = List.of(comment);

        when(commentRepository.findCommentsByPostId(post.getId()))
                .thenReturn(commentList);
        when(mapper.map(comment, CommentDTO.class)).thenReturn(commentDTO);

        ResponseEntity<List<CommentDTO>> response = underTest.getAllCommentsToAPost(post.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(commentList.size());
    }

    @Test
    void createNewComment() {
    }

    @Test
    void editComment() {
    }

    @Test
    void deleteComment() {
    }
}