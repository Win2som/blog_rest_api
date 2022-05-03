package com.example.blog_api.repositories;

import com.example.blog_api.models.Comment;
import com.example.blog_api.models.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository testRepo;
    @Autowired
    PostRepository postRepo;

//    @AfterEach
//    void tearDown() {
//        testRepo.deleteAll();
//    }

    @Test
    void findCommentsByPostId() {
        Post post = new Post();
        post.setTitle("imposter syndrome");
        post.setContent("knabhbhbhcknh");
        postRepo.save(post);
        Comment comment = new Comment();
        comment.setContent("comment test content");
        comment.setPost(post);
        testRepo.save(comment);


        List<Comment> foundComment = testRepo.findCommentsByPostId(post.getId());

        assertThat(comment).isIn(foundComment);

    }


}