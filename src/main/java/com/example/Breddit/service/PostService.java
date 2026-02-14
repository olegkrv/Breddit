package com.example.Breddit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.Post;

public interface PostService {
    List<Post> findAllPosts();

    void addPost(Post post, String title);

    Post updatePost(Post post);

    Post findById(Long id);

    boolean deletePost(Long id);
}
