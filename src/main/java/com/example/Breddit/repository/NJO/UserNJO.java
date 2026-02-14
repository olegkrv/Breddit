package com.example.Breddit.repository.NJO;

import java.beans.JavaBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import org.hibernate.annotations.processing.Exclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.UserReposiotry;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
/*
@AllArgsConstructor
@Component
public class UserNJO {
    private final UserReposiotry reposiotry;

    public String authUser(@RequestBody User user, CurrentUser CURRENT){
        User potetnial_user = reposiotry.findUserByemail(user.getEmail());

        if (potetnial_user == null) return "Такой почты не существует";
        
        String ur_password = potetnial_user.getPassword();

        if (user.getPassword().equals(ur_password) && ur_password != null){
            CURRENT.setUser(reposiotry.findUserByemail(user.getEmail()));
            return "".format("Рады видеть вас снова, %s", CURRENT.getNickname());
        }
        return "Неверный пароль";
    }
     
        public User deletePosts(String email, ArrayList<String> deletable_works){
        var user = reposiotry.findUserByid(email);
        
        if (user != null && !Collections.disjoint(user.getPosts(), deletable_works)){
            User user2 = user;
            ArrayList<String> new_works = user2.getPosts();
            user2.setPosts(new_works);
            return user2;
        }
        return null;
    }
}
*/