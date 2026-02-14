package com.example.Breddit.repository.DAO;

import lombok.RequiredArgsConstructor;


import java.util.ArrayList;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.example.Breddit.models.User;
//import com.example.Breddit.repository.NJO.UserNJO;


@Repository
@RequiredArgsConstructor
public class UserDAO {
    private final List<User> WRITERS = new ArrayList<>();
    //private UserNJO otherOperations;

  
    public List<User> findAllUsers(){
        // System.out.println(WRITERS);
        return WRITERS;
    }


    public User saveUser(User user){
        try {
            
        WRITERS.add(user);
        return user;}
        catch (Exception exception) {return null;}
    }

    public User findUserbyId(Long id){
        return WRITERS.stream().filter(id1 -> id1.getId().equals(id)).findFirst().orElse(null);
    }

    /*public User findUserbyEmail(String email){
        return WRITERS.stream().filter(email1 -> email1.getEmail().equals(email)).findFirst().orElse(null);
    }*/


    public User updateUser(User user){
        // Мб нужно вернуть var
        int userIndex = IntStream.range(0, WRITERS.size()).
        filter(element -> WRITERS.get(element).getId().equals(user.getId()))
        .findFirst().orElse(-1);
        
        if (userIndex > -1){
            WRITERS.set(userIndex, user);
            return user;
        }

        return null;
    }

    public boolean deleteUser(Long id){
        var user = findUserbyId(id);
        
        if (user != null){
            WRITERS.remove(user);
            return true;
        }
        return false;
    }

    /*ublic User addPosts(String email, ArrayList<String> adding_works){
        User user = otherOperations.addPosts(email, adding_works);
        if (user!= null) updateUser(user);
        return user;
    }

    public User deletePosts(String email, ArrayList<String> deletable_works){
        User user = otherOperations.deletePosts(email, deletable_works);
        if (user!= null) updateUser(user);
        return user;
    }*/

     
}

