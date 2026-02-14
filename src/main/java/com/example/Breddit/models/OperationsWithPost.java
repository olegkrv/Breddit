package com.example.Breddit.models;

import java.util.ArrayList;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class OperationsWithPost {
    private ArrayList<Long> posts = new ArrayList<Long>();
    
    public void addPost(Long id){
        ArrayList<Long> my_posts = this.getPosts();
        my_posts.add(id);
        this.setPosts(my_posts);
        my_posts = null;
    }
    
    public void deletePost(Long id){
        ArrayList<Long> my_posts = this.getPosts();
        my_posts.remove(id);
        this.setPosts(my_posts);
        my_posts = null;
    }
}
