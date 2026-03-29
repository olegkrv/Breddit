package com.example.Breddit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.Breddit.controllers.UserController;
import com.example.Breddit.models.Post;
import com.example.Breddit.models.Sub;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.Interfaces.PostPreRepository;
import com.example.Breddit.repository.Interfaces.SubPreRepository;
import com.example.Breddit.service.PostService;
import com.example.Breddit.service.SubService;
import com.example.Breddit.service.UsersService;
import com.example.Breddit.service.Impl.PostServiceImpl;
import com.example.Breddit.service.Impl.SubServiceImpl;

@SpringBootTest
public class SubServiceTest {
    private User Alex;
	private Object[] Alex_values = new Object[] {"Alex", "qwerty", "sample@example.com", LocalDate.parse("1987-03-11"), -1};

    private User Bill;
	private Object[] Bill_values = new Object[] {"Bill", "JR", "sstr@location.com", LocalDate.parse("1957-12-28"), 1};

    private User Cassady;
	private Object[] Cassady_values = new Object[] {"Cassady", "Fr3d", "jaw@bear.com", LocalDate.parse("1979-03-11"), 0};

    private User NonRegistredUser;
	private Object[] NRU_values = new Object[] {"void", "nothingness", "idk@what.com", LocalDate.parse("1971-11-24"), 0};

    private Sub coolSub = new Sub();

    @Mock
    private SubPreRepository repository;

    @Mock
    private PostPreRepository post_repository;

    @Mock
    private UsersService user_service;

    @Spy
    private UserController userController = new UserController(user_service, null, null, null);

    private SubService service;
    

    @BeforeEach
    void setUp(){
        service = new SubServiceImpl(repository, post_repository, userController, user_service);

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
        Mockito.when(user_service.updateUser(Cassady)).thenReturn(Cassady);
        Mockito.when(repository.findByid(1L)).thenReturn(coolSub);
        Mockito.when(repository.findBytitle("cuteCats")).thenReturn(coolSub);
        Mockito.when(repository.save(coolSub)).thenReturn(coolSub);

    }

    @Test
    void addSub(){
        userController.CURRENT.setUser(Cassady);
        assertEquals(service.addSub(coolSub).getMain_admin(), Cassady.getId());
        assertTrue(Cassady.getAdmined_subs().contains(coolSub.getId()));
        assertTrue(userController.CURRENT.getAdmined_subs().contains(coolSub.getId()));    
    }

    @Test
    void updateSub1(){
        HashMap<String, String> updating = new HashMap<>();
        updating.put("title", "veryCuteCats");
        assertEquals(service.updateSub(1L, updating).getTitle(), "veryCuteCats");
    }
    @Test
    void updateSub2(){
        HashMap<String, String> updating = new HashMap<>();
        updating.put("description", "wha");
        assertEquals(service.updateSub(1L, updating).getDescription(), "wha");
    }

    @Test
    void deleteSub_notOk(){
        userController.CURRENT.setUser(Cassady);
        assertEquals(service.deleteSub(2L), false);
    }

    @Test
    void deleteSub_Ok(){
        Cassady.addAdminedSub(1L);
        coolSub.setMain_admin(Cassady.getId());
        userController.CURRENT.setUser(Cassady);
        Post post = new Post();
        post.setAuthor(3L);
        Mockito.when(post_repository.findByid(1L)).thenReturn(post);
        Mockito.when(post_repository.findByid(2L)).thenReturn(post);
        Mockito.when(post_repository.findByid(3L)).thenReturn(post);
        Mockito.when(post_repository.findByid(4L)).thenReturn(post);


        coolSub.addPost(1L);
        coolSub.addPost(2L);
        coolSub.addPost(3L);

        Cassady.addPost(1L);
        Cassady.addPost(2L);
        Cassady.addPost(3L);
        Cassady.addPost(4L);
        

        assertTrue(service.deleteSub(1L));
        assertTrue(Cassady.getPosts().size() == 1 && Cassady.getPosts().contains(4L));
        assertTrue(Cassady.getAdmined_subs().isEmpty());

        assertTrue((userController.CURRENT.getPosts().size() == 1) && userController.CURRENT.getPosts().contains(4L));
        assertTrue(userController.CURRENT.getAdmined_subs().isEmpty());
    }

    @Test
    void adminOperations(){
        service.addAdmin(1L, 2L);
        service.addAdmin(1L, 3L);
        assertEquals(coolSub.getAdmins().size(), 2);

        service.removeAdmin(1L, 3L);
        assertEquals(coolSub.getAdmins().size(), 1);
        service.removeAdmin(1L, 2L);

        assertEquals(coolSub.getAdmins().size(), 0);
    }

    @Test
    void transferCrown(){
        userController.CURRENT.setUser(Cassady);
        service.transferCrown(1L, 3L);
        assertTrue(Cassady.getAdmined_subs().contains(coolSub.getId()));
        assertEquals(coolSub.getMain_admin(), Cassady.getId());
    }
}
