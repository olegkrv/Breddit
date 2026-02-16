package com.example.Breddit.repository.Interfaces;

import java.util.List;

import com.example.Breddit.models.Post;

public interface PostPreRepository {
    public List<Post> findAll();

    Post save(Post post);

    Post findByid(Long id);

    void deleteByid(Long id);
}
