package com.example.Breddit.repository.JPA;

import java.util.List;

import org.springframework.context.annotation.Primary;

import com.example.Breddit.models.User;
import com.example.Breddit.repository.UserReposiotry;
import com.example.Breddit.repository.Interfaces.UserPreRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Primary
public class UserJPA implements UserPreRepository {
    private final UserReposiotry reposiotry;

    public List<User> findAll(){ return reposiotry.findAll();}

    public User save(User user) { return reposiotry.save(user);}
    
    public User findUserByemail(String email){ return reposiotry.findUserByemail(email);}

    public User findUserByid(Long id){ return reposiotry.findUserByid(id);}

    public void deleteByid(Long id){ reposiotry.deleteByid(id);}
}
