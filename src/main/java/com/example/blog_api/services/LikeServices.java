package com.example.blog_api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface LikeServices {
    ResponseEntity<Long> likePost(long post_id, long customer_id);
    ResponseEntity<Long> unLikePost(long customer_id, long post_id);

    ResponseEntity<Long> likeComment(long customer_id, long comment_id);

    ResponseEntity<Long> unLikeComment(long customer_id, long comment_id);

    long getNoOfCommentLikes(long comment_id);

    long getNoOfPostLikes(long post_id);
}
