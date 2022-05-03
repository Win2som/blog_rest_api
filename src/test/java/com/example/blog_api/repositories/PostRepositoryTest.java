package com.example.blog_api.repositories;

import com.example.blog_api.models.User;
import com.example.blog_api.models.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PostRepositoryTest {


    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository customerRepository;


    @Test
    void findByIdAndCustomerId() {

        User user = new User();
        user.setFirstName("Chisom");
        user.setLastName("Obidike");
        user.setEmail("email@gmail.com");
        user.setPassword("1234");
        customerRepository.save(user);

        Post post = new Post();
        post.setContent("fresh post in the block");
        post.setTitle("poster");
        post.setUser(user);
        postRepository.save(post);

        Post foundPost = postRepository.findByIdAndUserId(post.getId(), user.getId());

        assertThat(foundPost).isEqualTo(post);


        Post post2 = new Post();
        post.setContent("fresh post in the block");
        post.setTitle("poster");
        postRepository.save(post);

        assertThat(foundPost).isNotEqualTo(post2);


    }
}