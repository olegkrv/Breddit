package com.example.Breddit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.Breddit.models.Sub;
import com.example.Breddit.repository.SubRepository;
import com.example.Breddit.repository.JPA.SubJPA;

@DataJpaTest
@EntityScan(basePackages = "com.example.Breddit.models")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SubRepositoryTest {
    private Sub coolSub = new Sub();
    private Sub bossFight = new Sub();

    @Autowired
    private SubRepository true_repo;

    private SubJPA repository;

    @BeforeEach
    void setUp() throws Exception{
        this.repository = new SubJPA(true_repo);
        coolSub.setTitle("cuteCats");
        coolSub.setDescription("smthng");

        bossFight.setTitle("bossfight");
        bossFight.setDescription("Home all of your bosses.");
    }

    @Test
    void save(){
        assertEquals(repository.save(coolSub), coolSub);
        assertEquals(repository.save(bossFight), bossFight);
    }

    @Test
    void findAll(){
        ArrayList<Sub> SUBS = new ArrayList<>();

        repository.save(coolSub);
        repository.save(bossFight);

        coolSub.setId(1L);
        bossFight.setId(2L);

        SUBS.add(coolSub);
        SUBS.add(bossFight);

        assertEquals(repository.findAll(), SUBS);
    }

    @Test
    void findById(){
        repository.save(coolSub);
        repository.save(bossFight);

        assertEquals(repository.findByid(1L), coolSub);
        assertEquals(repository.findByid(2L), bossFight);
    }

    @Test
    void findByTitle(){
        repository.save(coolSub);
        repository.save(bossFight);

        assertEquals(repository.findBytitle("cuteCats"), coolSub);
        assertEquals(repository.findBytitle("bossfight"), bossFight);
    }

    @Test
    void deleteById(){
        repository.save(coolSub);
        repository.save(bossFight);

        assertEquals(repository.findByid(1L), coolSub);
        assertEquals(repository.findByid(2L), bossFight);

        repository.deleteByid(1L);
        repository.deleteByid(2L);

        assertEquals(repository.findByid(1L), null);
        assertEquals(repository.findByid(2L), null);
    }
}
