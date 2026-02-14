package com.example.Breddit.service.JPA;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.usertype.UserCollectionType;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.Breddit.controllers.UserController;
import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.Post;
import com.example.Breddit.models.Sub;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.PostReposiroty;
import com.example.Breddit.service.PostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Primary
public class PostServiceJPA implements PostService{
    private final PostReposiroty reposiroty;
    private final UsersServiceJPA user_service;
    private final SubServiceJPA sub_service;
    private final UserController user_controller;

    @Override
    public List<Post> findAllPosts(){
        return reposiroty.findAll();
    }

    @Override
    public void addPost(Post post, String title){
        Sub this_sub = sub_service.findByTitle(title);

        post.setAuthor(user_controller.CURRENT.getId());
        post.setDate(Instant.now());
        post.setSub_id(this_sub.getId());

        Long new_post_id = reposiroty.save(post).getId();

        user_controller.CURRENT.addPost(new_post_id);

        User posting_man = user_service.findUserbyId(user_controller.CURRENT.getId());
        posting_man.addPost(new_post_id);
        user_service.updateUser(posting_man);
        posting_man = null;
        
        
        this_sub.addPost(new_post_id);
        sub_service.fullUpdate(this_sub);
        this_sub = null;
        
        new_post_id = null;
    }

    @Override
    public Post updatePost(Post post){
        return reposiroty.save(post);
    }

    @Override
    public Post findById(Long id){
        return reposiroty.findByid(id);
    }

    @Override
    public boolean deletePost(Long id){
        try{
           user_service.findUserbyId(reposiroty.findByid(id).getAuthor()).deletePost(id);
           sub_service.findById(reposiroty.findByid(id).getSub_id()).deletePost(id);
        reposiroty.deleteByid(id);
        return true; 
        }
        catch (Exception e){
            System.out.println("Ошибка удаления поста: " + e);
            return false;
        }
    }
}
