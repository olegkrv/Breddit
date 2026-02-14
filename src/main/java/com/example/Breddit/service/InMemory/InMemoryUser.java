package com.example.Breddit.service.InMemory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.example.Breddit.models.Code;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.DAO.UserDAO;
import com.example.Breddit.service.UsersService;

import lombok.AllArgsConstructor;
import lombok.val;

@Service
@AllArgsConstructor
// @Primary
public class InMemoryUser implements UsersService{
    private final UserDAO repository;

    @Override
    public List<User> findAllUsers(){
        return repository.findAllUsers();
    }

    @Override
    public User saveUser(User user){
        return repository.saveUser(user);
    }
    @Override
    public User findUserbyId(Long id){
        return repository.findUserbyId(id);
    }

    public User findUserbyEmail(String email){
        return null;
       /* return repository.findUserbyEmail(email);*/
    }

    @Override
    public User updateUser(User user){
        return repository.updateUser(user);
    }

    @Override
    public boolean deleteUser(Long id)
    {
        return repository.deleteUser(id);
    }

    @Override
    public Code saveCode(Code code){
        return null;
    }

    @Override
    public Code updateCode(Code code){
        return null;
    }
}

