package com.example.Breddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.example.Breddit.models.Post;
 
@EnableJpaRepositories
@Repository
public interface PostReposiroty extends JpaRepository<Post, Long> {
    public void deleteByid(Long id);

    public Post findByid(Long id);
}
