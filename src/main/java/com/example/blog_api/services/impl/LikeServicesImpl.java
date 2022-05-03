package com.example.blog_api.services.impl;

import com.example.blog_api.exception.ResourceNotFoundException;
import com.example.blog_api.models.Comment;
import com.example.blog_api.models.User;
import com.example.blog_api.models.Like;
import com.example.blog_api.models.Post;
import com.example.blog_api.repositories.CommentRepository;
import com.example.blog_api.repositories.UserRepository;
import com.example.blog_api.repositories.LikeRepository;
import com.example.blog_api.repositories.PostRepository;
import com.example.blog_api.services.LikeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LikeServicesImpl implements LikeServices {
        private final LikeRepository likeRepository;
        private final PostRepository postRepository;
        private final CommentRepository commentRepository;
        private final UserRepository userRepository;

        @Autowired
        public LikeServicesImpl(LikeRepository likeRepository, PostRepository postRepository,
                                CommentRepository commentRepository, UserRepository userRepository) {
            this.likeRepository = likeRepository;
            this.postRepository = postRepository;
            this.commentRepository = commentRepository;
            this.userRepository = userRepository;
        }

        @Override
        public ResponseEntity<Long> likePost(long post_id, long user_id) {
            Like liked = likeRepository.findByUserIdAndPostId(user_id, post_id);

            if(liked == null) {
                Like like = new Like();
                User user = userRepository.getById(user_id);
                Post post = postRepository.getById(post_id);
                like.setPost(post);
                like.setUser(user);
                likeRepository.save(like);
                post.setNoOfLikes(getNoOfPostLikes(post_id));
                postRepository.save(post);
            }
            return new ResponseEntity<>(getNoOfPostLikes(post_id), HttpStatus.CREATED);
        }


        @Override
        public ResponseEntity<Long> unLikePost(long user_id, long post_id) {
            Like like = likeRepository.findByUserIdAndPostId(user_id, post_id);
            if (like != null) {
                likeRepository.delete(like);
                Post post = postRepository.getById(post_id);
                post.setNoOfLikes(getNoOfPostLikes(post_id));
                postRepository.save(post);
                return new ResponseEntity(getNoOfPostLikes(post_id), HttpStatus.NO_CONTENT);
            }
            throw new ResourceNotFoundException("Post is already unliked");
        }


        @Override
        public ResponseEntity<Long> likeComment(long user_id, long comment_id) {
            Like liked = likeRepository.findByCommentIdAndUserId(comment_id, user_id);
            if (liked == null) {
                Like like = new Like();
                User user = userRepository.getById(user_id);
                Comment comment = commentRepository.findById(comment_id).get();
                like.setComment(comment);
                like.setUser(user);
                likeRepository.save(like);
                comment.setNoOflikes(getNoOfCommentLikes(comment_id));
                commentRepository.save(comment);
            }
            return new ResponseEntity<>(getNoOfCommentLikes(comment_id), HttpStatus.CREATED);
        }


        @Override
        public ResponseEntity<Long> unLikeComment(long user_id, long comment_id) {
            Like like = likeRepository.findByCommentIdAndUserId(comment_id, user_id);
            if (like != null) {
                likeRepository.delete(like);
                Comment comment = commentRepository.findById(comment_id).get();
                comment.setNoOflikes(getNoOfCommentLikes(comment_id));
                commentRepository.save(comment);
                return new ResponseEntity(getNoOfCommentLikes(comment_id), HttpStatus.NO_CONTENT);
            }
            throw new ResourceNotFoundException("Comment is already unliked");
        }


        @Override
        public long getNoOfCommentLikes(long comment_id) {
            return likeRepository.findAllByCommentId(comment_id).size();
        }


        @Override
        public long getNoOfPostLikes(long post_id) {
            return likeRepository.findAllByPostId(post_id).size();
        }

}
