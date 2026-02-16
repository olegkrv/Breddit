package com.example.Breddit.repository.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Repository;

import com.example.Breddit.models.Sub;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.Interfaces.SubPreRepository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class SubDAO implements SubPreRepository{
    private final List<Sub> SUBS = new ArrayList<>();

  
    public List<Sub> findAll(){
        return SUBS;
    }


    public Sub save(Sub sub){
        try {
        int subIndex = IntStream.range(0, SUBS.size()).
        filter(element -> SUBS.get(element).getId().equals(sub.getId()))
        .findFirst().orElse(-1);

        if (subIndex > -1){
            SUBS.set(subIndex, sub);
            return sub;
        }
  
        SUBS.add(sub);
        return sub;}

        catch (Exception exception) {return null;}
    }

    public Sub findByid(Long id){
        return SUBS.stream().filter(id1 -> id1.getId().equals(id)).findFirst().orElse(null);
    }

    public Sub findBytitle(String title){
        return SUBS.stream().filter(email1 -> email1.getTitle().equals(title)).findFirst().orElse(null);
    }


    public void deleteByid(Long id){
        var post = findByid(id);
         
        if (post != null) SUBS.remove(post);
    }
}
