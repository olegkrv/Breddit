package com.example.Breddit.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.Breddit.controllers.PostController;
import com.example.Breddit.models.Code;
import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.CodeRepository;
import com.example.Breddit.repository.UserReposiotry;


public interface UsersService{
    List<User> findAllUsers();

    String saveUser(User user);

    User findUserbyId(Long id);

    User findUserbyEmail(String email);

    User updateUser(User user);

    boolean deleteUser(Long id);

    Code saveCode(Code code);

    Code updateCode(Code code);
    
    Code generateCode(String email);
    
    Code TwoFactorsAuth(String email,JavaMailSender sender);

    String authUser(User user, User potetnial_user);

    boolean verification(User user, String ur_code,Code this_code);
    
    String logOut();

    String makeAdmin(Long id);
   
}
    

