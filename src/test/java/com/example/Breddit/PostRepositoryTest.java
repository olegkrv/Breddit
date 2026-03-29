package com.example.Breddit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.Breddit.models.Post;
import com.example.Breddit.models.Sub;
import com.example.Breddit.repository.PostReposiroty;
import com.example.Breddit.repository.SubRepository;
import com.example.Breddit.repository.JPA.PostJPA;
import com.example.Breddit.repository.JPA.SubJPA;

@DataJpaTest
@EntityScan(basePackages = "com.example.Breddit.models")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {
    private Post hello = new Post("I'm gonna say hello", "HELLO");
    private Post bye = new Post("I'm gonna say bye", "BYE");

    @Autowired
    private PostReposiroty true_repo;

    private PostJPA repository;

    @BeforeEach
    void setUp() throws Exception{
        this.repository = new PostJPA(true_repo);
    }

    @Test
    void save(){
        assertEquals(repository.save(hello), hello);
        assertEquals(repository.save(bye), bye);
    }

    @Test
    void findAll(){
        ArrayList<Post> POSTS = new ArrayList<>();

        repository.save(hello);
        repository.save(bye);

        hello.setId(1L);
        bye.setId(2L);

        POSTS.add(hello);
        POSTS.add(bye);

        assertEquals(repository.findAll(), POSTS);
    }

    @Test
    void findById(){
        repository.save(hello);
        repository.save(bye);

        assertEquals(repository.findByid(1L), hello);
        assertEquals(repository.findByid(2L), bye);
    }

    @Test
    void deleteById(){
        repository.save(hello);
        repository.save(bye);

        assertEquals(repository.findByid(1L), hello);
        assertEquals(repository.findByid(2L), bye);

        repository.deleteByid(1L);
        repository.deleteByid(2L);

        assertEquals(repository.findByid(1L), null);
        assertEquals(repository.findByid(2L), null);
    }
}
