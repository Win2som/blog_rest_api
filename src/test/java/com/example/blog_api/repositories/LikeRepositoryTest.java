package com.example.blog_api.repositories;

import com.example.blog_api.models.Comment;
import com.example.blog_api.models.User;
import com.example.blog_api.models.Like;
import com.example.blog_api.models.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class LikeRepositoryTest {
    @Autowired
    UserRepository customerRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    CommentRepository commentRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void findByCustomerIdAndPostId() {
        User user = new User();
        user.setFirstName("Chisom");
        user.setLastName("Obidike");
        user.setEmail("email@gmail.com");
        user.setPassword("1234");
        customerRepository.save(user);

        Post post = new Post();
        post.setContent("fresh post in the block");
        post.setTitle("poster");
        postRepository.save(post);

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);

        Like liked = likeRepository.findByUserIdAndPostId(user.getId(), post.getId());

        assertThat(liked).isEqualTo(like);
    }

    @Test
    void findAllByCommentId() {
        Comment comment = new Comment();
        comment.setContent("what's up");
        commentRepository.save(comment);

        Like like =new Like();
        like.setComment(comment);
        likeRepository.save(like);
        Like like2 = new Like();
        like2.setComment(comment);
        likeRepository.save(like2);
        Like like3 = new Like();
        like3.setComment(comment);
        likeRepository.save(like3);
        Like like4 = new Like();
        likeRepository.save(like4);

        Collection<Like> likes = likeRepository.findAllByCommentId(comment.getId());

        assertThat(like).isIn(likes);
        assertThat(like2).isIn(likes);
        assertThat(like3).isIn(likes);
        assertThat(like4).isNotIn(likes);

        int size = likes.size();

        assertThat(size).isEqualTo(3);


    }

    @Test
    void findAllByPostId() {

        Post post = new Post();
        post.setTitle("post for test");
        post.setContent("jabdbvliijubvfnsnjnjsna");
        postRepository.save(post);

        Like like =new Like();
        like.setPost(post);
        likeRepository.save(like);
        Like like2 = new Like();
        like2.setPost(post);
        likeRepository.save(like2);
        Like like3 = new Like();
        like3.setPost(post);
        likeRepository.save(like3);
        Like like4 = new Like();
        likeRepository.save(like4);

        Collection<Like> likes = likeRepository.findAllByPostId(post.getId());

        assertThat(like).isIn(likes);
        assertThat(like2).isIn(likes);
        assertThat(like3).isIn(likes);
        assertThat(like4).isNotIn(likes);

        int size = likes.size();

        assertThat(size).isEqualTo(3);


    }

    @Test
    void findByCommentIdAndCustomerId() {
        User user = new User();
        user.setFirstName("Chisom");
        user.setLastName("Obidike");
        user.setEmail("email@gmail.com");
        user.setPassword("1234");
        customerRepository.save(user);

        Comment comment = new Comment();
        comment.setContent("what's up");
        commentRepository.save(comment);

        Like like = new Like();
        like.setUser(user);
        like.setComment(comment);
        likeRepository.save(like);

        Like foundLike = likeRepository.findByCommentIdAndUserId(comment.getId(), user.getId());

        assertThat(foundLike).isEqualTo(like);

    }
}