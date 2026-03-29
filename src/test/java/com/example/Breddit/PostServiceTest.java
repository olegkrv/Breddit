package com.example.Breddit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;

import org.hibernate.engine.spi.CascadingAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.Breddit.controllers.UserController;
import com.example.Breddit.models.Post;
import com.example.Breddit.models.Sub;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.Interfaces.PostPreRepository;
import com.example.Breddit.service.PostService;
import com.example.Breddit.service.SubService;
import com.example.Breddit.service.UsersService;
import com.example.Breddit.service.Impl.PostServiceImpl;

@SpringBootTest
public class PostServiceTest {
     private User Alex;
	private Object[] Alex_values = new Object[] {"Alex", "qwerty", "sample@example.com", LocalDate.parse("1987-03-11"), -1};

    private User Bill;
	private Object[] Bill_values = new Object[] {"Bill", "JR", "sstr@location.com", LocalDate.parse("1957-12-28"), 1};

    private User Cassady;
	private Object[] Cassady_values = new Object[] {"Cassady", "Fr3d", "jaw@bear.com", LocalDate.parse("1979-03-11"), 0};

    private User NonRegistredUser;
	private Object[] NRU_values = new Object[] {"void", "nothingness", "idk@what.com", LocalDate.parse("1971-11-24"), 0};

    private Sub coolSub = new Sub();

    @Spy
    private UserController userController = new UserController(null, null, null, null);

    @Mock
    private UsersService user_service;

    @Mock
    private SubService sub_service;

    @Mock
    private PostPreRepository repository;

    PostService service;

    @BeforeEach
    void setUp(){
        this.service = new PostServiceImpl(repository, user_service, sub_service, userController);

        this.Alex = new User();
        Alex.setUser(1L, Alex_values);

        this.Bill = new User();
        Bill.setUser(2L, Bill_values);

        this.Cassady = new User();
        Cassady.setUser(3L, Cassady_values);
        Mockito.when(user_service.findUserbyId(3L)).thenReturn(Cassady);

        this.NonRegistredUser = new User();
        NonRegistredUser.setUser(null, NRU_values);

        coolSub.setId(1L);
        coolSub.setTitle("cuteCats");
        coolSub.setDescription("smthng");

        Mockito.when(user_service.findUserbyId(3L)).thenReturn(Cassady);
        Mockito.when(sub_service.findById(1L)).thenReturn(coolSub);
        Mockito.when(sub_service.findByTitle("cuteCats")).thenReturn(coolSub);

    }

    @Test
    void addPost(){
        Post artificial = new Post("Unpopular opinion", "I love cats!");
        Post natural = new Post("Unpopular opinion", "I love cats!");

        userController.CURRENT.setUser(Cassady);

        artificial.setAuthor(3L);
        artificial.setSub_id(1L);

        Mockito.when(repository.save(natural)).thenAnswer(invocation -> {natural.setId(1L); return natural;});
        artificial.setId(1L);
        Mockito.when(sub_service.fullUpdate(coolSub)).thenReturn(coolSub);
        service.addPost(natural, "cuteCats");
        artificial.setDate(natural.getDate());
        assertEquals(artificial, natural);
        assertTrue(coolSub.getPosts().contains(1L));
    }

    @Test
    void deletePost_notOk(){
        assertEquals(service.deletePost(1L), false);
    }

    @Test
    void deletePost_Ok(){
        Post post = new Post("Unpopular opinion", "I love cats!");
        post.setId(1L);
        Mockito.when(repository.findByid(1L)).thenReturn(post);

        userController.CURRENT.setUser(Cassady);
        Mockito.when(repository.save(post)).thenReturn(post); 
        service.addPost(post, "cuteCats");

        ArrayList<Long> posts = new ArrayList<>();
        posts.add(1L);
        
        assertEquals(Cassady.getPosts(), posts);
        assertEquals(coolSub.getPosts(), posts);

        service.deletePost(1L);
        assertTrue(Cassady.getPosts().isEmpty());
        assertTrue(coolSub.getPosts().isEmpty());
    }
     
}
