package com.example.Breddit.repository.Interfaces;

import java.util.List;

import com.example.Breddit.models.User;

public interface UserPreRepository{
    List<User> findAll();

    User save(User user);
    
    User findUserByemail(String email);

    User findUserByid(Long id);

    void deleteByid(Long id);
}
