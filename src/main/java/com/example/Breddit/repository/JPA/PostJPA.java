package com.example.Breddit.repository.JPA;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.context.annotation.Primary;

import com.example.Breddit.models.Post;
import com.example.Breddit.repository.PostReposiroty;
import com.example.Breddit.repository.Interfaces.PostPreRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Primary
public class PostJPA implements PostPreRepository {
    private final PostReposiroty repository;

    public List<Post> findAll(){
        return repository.findAll();
    }


    public Post save(Post post){
        return repository.save(post);
    }

    public Post findByid(Long id){
        return repository.findByid(id);
    }



    public void deleteByid(Long id){
        repository.deleteByid(id);
    }
}
