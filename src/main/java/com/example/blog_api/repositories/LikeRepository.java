package com.example.blog_api.repositories;

import com.example.blog_api.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserIdAndPostId(long user_id, long post_id);

    Collection<Like> findAllByCommentId(long comment_id);

    Collection<Like> findAllByPostId(long post_id);


    Like findByCommentIdAndUserId(long comment_id, long user_id);

}

