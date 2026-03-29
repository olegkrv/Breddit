package com.example.Breddit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.Breddit.controllers.PostController;
import com.example.Breddit.controllers.UserController;
import com.example.Breddit.models.Code;
import com.example.Breddit.models.Post;
import com.example.Breddit.models.Sub;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.CodeRepository;
import com.example.Breddit.repository.SubRepository;
import com.example.Breddit.service.PostService;
import com.example.Breddit.service.SubService;
import com.example.Breddit.service.UsersService;

@SpringBootTest
public class PostControllerTest {
    private User Alex;
	private Object[] Alex_values = new Object[] {"Alex", "qwerty", "sample@example.com", LocalDate.parse("1987-03-11"), -1};

    private User Bill;
	private Object[] Bill_values = new Object[] {"Bill", "JR", "sstr@location.com", LocalDate.parse("1957-12-28"), 1};

    private User Cassady;
	private Object[] Cassady_values = new Object[] {"Cassady", "Fr3d", "jaw@bear.com", LocalDate.parse("1979-03-11"), 0};

    private User Will = new User();
	private Object[] Will_values = new Object[] {"Will", "FamilyGuy", "brthr@location.com", LocalDate.parse("1961-12-28"), 2};

    private User NonRegistredUser;
	private Object[] NRU_values = new Object[] {"void", "nothingness", "idk@what.com", LocalDate.parse("1971-11-24"), 0};

    private Sub coolSub = new Sub();

    @Mock
	private PostService service;

    @Mock 
    private SubService sub_service;


    @Spy
    private UserController userController = new UserController(null, null, null, null);

	@InjectMocks
    private PostController postController;


    

	@BeforeEach
	void setUp() throws Exception{
        this.Alex = new User();
        Alex.setUser(1L, Alex_values);

        this.Bill = new User();
        Bill.setUser(2L, Bill_values);

        this.Cassady = new User();
        Cassady.setUser(3L, Cassady_values);

        this.Will = new User();
        Will.setUser(4L, Will_values);

        this.NonRegistredUser = new User();
        NonRegistredUser.setUser(null, NRU_values);

        coolSub.setId(1l);
        coolSub.setTitle("cuteCats");
        coolSub.setDescription("smthng");
        Mockito.when(sub_service.findByTitle("cuteCats")).thenReturn(coolSub);
        
    }

    @Test
    void addPost_Banned(){
        Post post = new Post();

        userController.CURRENT.setUser(Alex);
        assertEquals(postController.addPost(post, null), "Вы забанены.");
        
    }

    @Test
    void addPost_noTitleOfSub(){
        Post post = new Post(null, "I love cats!");

        userController.CURRENT.setUser(Cassady);
        assertEquals(postController.addPost(post, null),  "Вы не выбрали Саббреддит!");
        
    }

    @Test
    void addPost_noSub(){
        Post post = new Post(null, "I love cats!");

        userController.CURRENT.setUser(Cassady);
        assertEquals(postController.addPost(post, "badCats"),  "Такого Саббреддита не существует!");
    }

    @Test 
    void addPost_notLogIn(){
         Post post = new Post(null, "I love cats!");

        assertEquals(postController.addPost(post, "cuteCats"),  "Необходимо войти в аккаунт, чтобы сделать пост!");
    }

    @Test
    void addPost_EmptyTitle1(){
        Post post = new Post(null, "I love cats!");

        userController.CURRENT.setUser(Cassady);
        assertEquals(postController.addPost(post, "cuteCats"),  "Заголовок не может быть пустым!");
    }

    @Test
    void addPost_EmptyTitle2(){
        Post post = new Post("", "I love cats!");

        userController.CURRENT.setUser(Cassady);
        assertEquals(postController.addPost(post, "cuteCats"),  "Заголовок не может быть пустым!");
    }

    @Test
    void addPost_EmptyTitle3(){
        Post post = new Post("     ", "I love cats!");

        userController.CURRENT.setUser(Cassady);
        assertEquals(postController.addPost(post, "cuteCats"),  "Заголовок не может быть пустым!");
    }

    @Test
    void addPost_Ok(){
        Post post = new Post("Unpopular opinion", "I love cats!");

        userController.CURRENT.setUser(Cassady);
        assertEquals(postController.addPost(post, "cuteCats"),  "Пост успешно добавлен!");
    }

    @Test
    void updatePost(){
        Post post = new Post("Unpopular opinion", "I love cats!");

        Mockito.when(service.updatePost(post)).thenReturn(post);

        userController.CURRENT.setUser(Alex);
        assertEquals(postController.updatePost(post), null);

        userController.CURRENT.setUser(Cassady);
        assertEquals(postController.updatePost(post), post);

        userController.CURRENT.setUser(Bill);
        assertEquals(postController.updatePost(post), post);

        userController.CURRENT.setUser(Will);
        assertEquals(postController.updatePost(post), post);
    }

    @Test
    void findPostById(){
        Post post = new Post("Unpopular opinion", "I love cats!");
        post.setId(1L);

        Mockito.when(service.findById(1L)).thenReturn(post);

        userController.CURRENT.setUser(Alex);
        assertEquals(postController.findById(1L), null);

        userController.CURRENT.setUser(Cassady);
        assertEquals(postController.findById(1L), post);

        userController.CURRENT.setUser(Bill);
        assertEquals(postController.findById(1L), post);

        userController.CURRENT.setUser(Will);
        assertEquals(postController.findById(1L), post);
    }

    @Test
    void deletePostById_Banned(){
        userController.CURRENT.setUser(Alex);
        assertEquals(postController.deletePost(1L), "Вы забанены.");
    }

    @Test
    void deletePostById_notLogIn(){
        Post post = new Post("Unpopular opinion", "I love cats!");
        post.setId(1L);

        Mockito.when(service.findById(1L)).thenReturn(post);

        assertEquals(postController.deletePost(1L), "Вы даже не авторизовались!");
    }

    @Test
    void deletePostById_giveItBack_Thief(){
        Post post = new Post("Unpopular opinion", "I love cats!");
        post.setId(1L);
        post.setAuthor(2L);
        
        Mockito.when(service.findById(1L)).thenReturn(post);

        userController.CURRENT.setUser(Cassady);
        assertEquals(postController.deletePost(1L), "Удалять можно лишь свои посты!");
    }

    @Test
    void deletePostById_noPost(){
        assertEquals(postController.deletePost(1L), "Поста с таким id не существует!");
    }

    @Test
    void deletePostById_Ok(){
        Post post = new Post("Unpopular opinion", "I love cats!");
        post.setId(1L);
        post.setAuthor(3L);
        
        Mockito.when(service.findById(1L)).thenReturn(post);
        Mockito.when(service.deletePost(1L)).thenReturn(true);

        userController.CURRENT.setUser(Cassady);
        assertEquals(postController.deletePost(1L), "Пост успешно удалён!");
    }

}
