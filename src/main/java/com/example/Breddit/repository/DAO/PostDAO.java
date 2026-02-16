package com.example.Breddit.repository.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Repository;

import com.example.Breddit.models.Post;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.Interfaces.PostPreRepository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class PostDAO implements PostPreRepository {
     private final List<Post> POSTS = new ArrayList<>();

  
    public List<Post> findAll(){
        return POSTS;
    }


    public Post save(Post post){
        try {
        int postIndex = IntStream.range(0, POSTS.size()).
        filter(element -> POSTS.get(element).getId().equals(post.getId()))
        .findFirst().orElse(-1);

        if (postIndex > -1){
            POSTS.set(postIndex, post);
            return post;
        }
  
        POSTS.add(post);
        return post;}

        catch (Exception exception) {return null;}
    }

    public Post findByid(Long id){
        return POSTS.stream().filter(id1 -> id1.getId().equals(id)).findFirst().orElse(null);
    }



    public void deleteByid(Long id){
        var post = findByid(id);
         
        if (post != null) POSTS.remove(post);
    }
}
