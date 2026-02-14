package com.example.Breddit.repository;


import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.example.Breddit.models.User;


@EnableJpaRepositories
@Repository

public interface UserReposiotry extends JpaRepository<User, Long>{
    public void deleteByid(Long id);

    public User findUserByid(Long id);

    public User findUserByemail(String email);

    // public User updateByid(String email);
}
