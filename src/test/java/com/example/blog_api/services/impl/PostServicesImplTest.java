package com.example.blog_api.services.impl;

import com.example.blog_api.dtos.PostDTO;
import com.example.blog_api.dtos.RegDTO;
import com.example.blog_api.exception.ResourceAlreadyExistException;
import com.example.blog_api.exception.ResourceNotFoundException;
import com.example.blog_api.models.Category;
import com.example.blog_api.models.User;
import com.example.blog_api.models.Post;
import com.example.blog_api.repositories.CategoryRepository;
import com.example.blog_api.repositories.UserRepository;
import com.example.blog_api.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServicesImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private PostServicesImpl underTest;

    private Post post;
    private PostDTO postDTO;

    private RegDTO regDTO;
    private User user;
    private Category category;

    @BeforeEach
    void setUp(){

        postDTO = new PostDTO();
        postDTO.setTitle("New Arrival");
        postDTO.setContent("Everything");

        post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setUser(new User());


        regDTO = new RegDTO();
        regDTO.setFirstName("Chisom");
        regDTO.setLastName("Obidike");
        regDTO.setEmail("obidike@yahoo.com");
        regDTO.setPassword("1234");


        user = new User();
        user.setFirstName(regDTO.getFirstName());
        user.setLastName(regDTO.getLastName());
        user.setEmail(regDTO.getEmail());
        user.setPassword(regDTO.getPassword());
        user.setDateRegistered(LocalDate.now());

        category = new Category();
    }

    @Test
    void canGetAllPosts() {
        List<Post> postList = List.of(post);
        when(postRepository.findAll()).thenReturn(postList);
        when(mapper.map(post, PostDTO.class)).thenReturn(postDTO);

        ResponseEntity<List<PostDTO>> response = underTest.getAllPosts();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(postList.size());
    }

    @Test
    void canGetASinglePost() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.ofNullable(post));
        when(mapper.map(post, PostDTO.class)).thenReturn(postDTO);
        ResponseEntity<PostDTO> response = underTest.getPost(post.getId());

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo(post.getTitle());
        assertThat(response.getBody().getContent()).isEqualTo(post.getContent());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }


    @Test
    void canCreateNewPost() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.ofNullable(category));


        when(mapper.map(postDTO, Post.class)).thenReturn(post);
        when(mapper.map(post, PostDTO.class)).thenReturn(postDTO);

        ResponseEntity<PostDTO> response = underTest.createNewPost(user.getId(), category.getId(),postDTO);

        assertThat(response.getBody()).isNotNull();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody()).isEqualTo(postDTO);

    }

    @Test
    void editPost() {

        when(postRepository.findById(post.getId())).thenReturn(Optional.ofNullable(post));

        when(mapper.map(postDTO, Post.class)).thenReturn(post);
        when(mapper.map(post, PostDTO.class)).thenReturn(postDTO);

        ResponseEntity<PostDTO> response = underTest.editPost(post.getId(),user.getId(),postDTO);

        assertThat(response.getBody()).isEqualTo(postDTO);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deletePost() {
        when(postRepository.findByIdAndUserId(post.getId(), post.getUser().getId())).thenReturn(post);

        when(mapper.map(post, PostDTO.class)).thenReturn(postDTO);

        ResponseEntity<PostDTO> response = underTest.deletePost(post.getId(), post.getUser().getId());

        assertThat(response.getBody()).isEqualTo(postDTO);
    }

    @Test
    void shouldThrowWhenPostIsNull() {
        when(postRepository.findByIdAndUserId(post.getId(), post.getUser().getId())).thenReturn(null);

        assertThatThrownBy(() -> underTest.deletePost(post.getId(), post.getUser().getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Post not deleted because post does not exist or you are not authorised to delete this post");
    }
}