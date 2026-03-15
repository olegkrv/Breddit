package com.example.Breddit.repository.JPA;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.context.annotation.Primary;

import com.example.Breddit.models.Sub;
import com.example.Breddit.repository.SubRepository;
import com.example.Breddit.repository.Interfaces.SubPreRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Primary
public class SubJPA implements SubPreRepository{
    private final SubRepository repository;
  
    public List<Sub> findAll(){
        return repository.findAll();
    }


    public Sub save(Sub sub){ 
        return repository.save(sub);
    }

    public Sub findByid(Long id){
        return repository.findByid(id);
    }

    public Sub findBytitle(String title){
        return repository.findBytitle(title);
    }


    public void deleteByid(Long id){
        repository.deleteByid(id);
    }
}
