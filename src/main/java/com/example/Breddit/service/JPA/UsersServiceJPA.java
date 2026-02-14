package com.example.Breddit.service.JPA;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.Breddit.models.Code;
import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.CodeRepository;
import com.example.Breddit.repository.PostReposiroty;
import com.example.Breddit.repository.SubRepository;
import com.example.Breddit.repository.UserReposiotry;
import com.example.Breddit.service.SubService;
//import com.example.Breddit.repository.NJO.UserNJO;
import com.example.Breddit.service.UsersService;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@RequiredArgsConstructor
@Primary
public class UsersServiceJPA implements UsersService{
    private final UserReposiotry repository;
    private final PostReposiroty post_repository;
    private final SubRepository sub_repository;
    private final CodeRepository code_repository;

    
    @Override
    public List<User> findAllUsers(){
        try{return repository.findAll();}
        catch (Exception exception){System.out.println(exception); return null;}
        //return repository.findAll();
    }

    @Override
    public User saveUser(User user){
        try{System.out.println(user);
            return repository.save(user);}
        catch (Exception exception) {
            System.out.println(exception);
            return null;}
    }
    @Override
    public User findUserbyEmail(String email){
        return repository.findUserByemail(email);
    }

    @Override
    public User findUserbyId(Long id){
        return repository.findUserByid(id);
    }

    @Override
    public User updateUser(User user){
        return repository.save(user);
    }

    @Override
    @Transactional
    public boolean deleteUser(Long id)
    {
        if (repository.findUserByid(id) != null) {
            for (Long post: repository.findUserByid(id).getPosts()) {
                sub_repository.findByid(post_repository.findByid(post).getSub_id()).deletePost(post);
                post_repository.deleteByid(post);
            }
        
            repository.deleteByid(id); 
            return true;
        }
        return false;
    }

    @Override
    public Code saveCode(Code code){
        if (code_repository.findByvalue(code.getValue()) != null) return null;
        return code_repository.save(code);
    }

    @Override
    public Code updateCode(Code code){
        return code_repository.save(code);
    }

}
