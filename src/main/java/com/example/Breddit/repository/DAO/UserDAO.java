package com.example.Breddit.repository.DAO;

import lombok.RequiredArgsConstructor;


import java.util.ArrayList;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.example.Breddit.models.User;
//import com.example.Breddit.repository.NJO.UserNJO;
import com.example.Breddit.repository.Interfaces.UserPreRepository;


@Repository
@RequiredArgsConstructor
public class UserDAO implements UserPreRepository {
    private final List<User> USERS = new ArrayList<>();

  
    public List<User> findAll(){
        return USERS;
    }


    public User save(User user){
        try {
        int userIndex = IntStream.range(0, USERS.size()).
        filter(element -> USERS.get(element).getId().equals(user.getId()))
        .findFirst().orElse(-1);

        if (userIndex > -1){
            USERS.set(userIndex, user);
            return user;
        }
  
        USERS.add(user);
        return user;}

        catch (Exception exception) {return null;}
    }

    public User findUserByid(Long id){
        return USERS.stream().filter(id1 -> id1.getId().equals(id)).findFirst().orElse(null);
    }

    public User findUserByemail(String email){
        return USERS.stream().filter(email1 -> email1.getEmail().equals(email)).findFirst().orElse(null);
    }


    public void deleteByid(Long id){
        var user = findUserByid(id);
         
        if (user != null) USERS.remove(user);
    }
  
}

