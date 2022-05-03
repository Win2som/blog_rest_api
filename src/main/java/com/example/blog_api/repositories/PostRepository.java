package com.example.blog_api.repositories;

import com.example.blog_api.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    List<Post> findAll();

//    Post findById(long post_id);


//    void delete(Post post);

    Post findByIdAndUserId(long post_id, long user_id);
}
