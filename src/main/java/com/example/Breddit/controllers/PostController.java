package com.example.Breddit.controllers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.hibernate.internal.util.SubSequence;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.example.Breddit.models.Post;
import com.example.Breddit.models.Sub;
import com.example.Breddit.models.User;
import com.example.Breddit.service.SubService;
import com.example.Breddit.service.UsersService;
import com.example.Breddit.service.JPA.PostServiceJPA;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/br/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PostController {
    private final PostServiceJPA service;
    private final SubService sub_service;

    private final UserController user_controller;
    // private final UsersService user_service;

    @GetMapping("/posts")
    public List<Post> findAllPosts(){
        return service.findAllPosts();
    }

    @PostMapping("{title}/add_post")
    public String addPost(@RequestBody Post post, @PathVariable String title){
        try{
           if (user_controller.CURRENT.getStatus().equals(-1)) return "Вы забанены."; 

           if (title == null) return "Вы не выбрали Саббреддит!";
           else if (sub_service.findByTitle(title) == null) return "Такого Саббреддита не существует!"; 
           if (user_controller.CURRENT.getId() == null) return "Необходимо войти в аккаунт, чтобы сделать пост!";
           
        else if (post.getTitle().split(" ").length == 0 || post.getTitle().equals("")) return "Заголовок не может быть пустым!";
        
        service.addPost(post, title);
        
        return "Пост успешно добавлен!"; 
        }
        
        
        catch (Exception e) {return "Ошибка добавления поста: " + e;}
    }

    @PutMapping("/update_post")
    public Post updatePost(@RequestBody Post post){
        if (user_controller.CURRENT.getStatus().equals(-1)) return null; 
        return service.updatePost(post);
    }

    @GetMapping("{title}/{id}")
    public Post findById(@PathVariable Long id){
        if (user_controller.CURRENT.getStatus().equals(-1)) return null; 
        return service.findById(id);
    }

    @Transactional
    @DeleteMapping("delete_post/{id}")
    public String deletePost(@PathVariable Long id){
        if (user_controller.CURRENT.getStatus().equals(-1)) return "Вы забанены."; 

        if (service.findById(id) != null) {
            if (user_controller.CURRENT.getId() == null) return "Вы даже не авторизовались!";
            if (!(service.findById(id).getAuthor().equals(user_controller.CURRENT.getId()))){
            return "Удалять можно лишь свои посты!";
        }
        if (service.deletePost(id)){
            return "Пост успешно удалён!";
        }
    }
        return "Поста с таким id не существует!";
    }
    
}
