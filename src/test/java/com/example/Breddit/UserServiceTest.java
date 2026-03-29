package com.example.Breddit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.Breddit.models.Code;
import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.Sub;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.CodeRepository;
import com.example.Breddit.repository.Interfaces.PostPreRepository;
import com.example.Breddit.repository.Interfaces.SubPreRepository;
import com.example.Breddit.repository.Interfaces.UserPreRepository;
import com.example.Breddit.service.UsersService;
import com.example.Breddit.service.Impl.UserServiceImpl;

import jakarta.inject.Inject;

@SpringBootTest
public class UserServiceTest {
    private User Alex;
	private Object[] Alex_values = new Object[] {"Alex", "qwerty", "sample@example.com", LocalDate.parse("1987-03-11"), -1};

    private User Bill;
	private Object[] Bill_values = new Object[] {"Bill", "JR", "sstr@location.com", LocalDate.parse("1957-12-28"), 1};

    private User Cassady;
	private Object[] Cassady_values = new Object[] {"Cassady", "Fr3d", "jaw@bear.com", LocalDate.parse("1979-03-11"), 0};

    private User NonRegistredUser;
	private Object[] NRU_values = new Object[] {"void", "nothingness", "idk@what.com", LocalDate.parse("1971-11-24"), 0};

    private Sub coolSub = new Sub();
    private Code goodCode = new Code("12345");
    private Code nonSavedCode = new Code("63845");
    CurrentUser CURRENT = new CurrentUser();


    @Mock
    private UserPreRepository repository;

    @Mock
    private SubPreRepository sub_repository;

    @Mock
    private PostPreRepository post_repository;
    
    @Mock
    private CodeRepository code_repository;

    @Mock
    private JavaMailSender sender;
    

    //@InjectMocks
    UsersService service;

    //@InjectMocks
    //UserServiceImpl service;

    @BeforeEach
    void setUp() throws Exception{
        this.service = new UserServiceImpl(repository, code_repository, post_repository, sub_repository, sender);

        this.Alex = new User();
        Alex.setUser(1L, Alex_values);

        this.Bill = new User();
        Bill.setUser(2L, Bill_values);

        this.Cassady = new User();
        Cassady.setUser(3L, Cassady_values);

        this.NonRegistredUser = new User();
        NonRegistredUser.setUser(null, NRU_values);

        Mockito.when(repository.findUserByid(3L)).thenReturn(Cassady);

        Mockito.when(code_repository.findByvalue("12345")).thenReturn(goodCode);

        Mockito.when(code_repository.save(nonSavedCode)).thenReturn(nonSavedCode);
    }

    @Test
    void deleteUser_notOk(){
        assertEquals(service.deleteUser(4L), false);
    }

    @Test
    void deleteUser_Ok(){
        assertEquals(service.deleteUser(3L), true);
    }

    @Test
    void saveCode_notOk(){
        assertEquals(service.saveCode(goodCode), null);
    }

    @Test
    void saveCode_Ok(){
        assertEquals(service.saveCode(nonSavedCode), nonSavedCode);
    }

    @Test
    void logOut(){
        CurrentUser JaneDoe = new CurrentUser();
        
        JaneDoe.setId(null);
        JaneDoe.setPassed(false);
        JaneDoe.setNickname(null);
        JaneDoe.setPassword(null);
        //Class<?> JClass = JaneDoe.getClass();

        Field[] fields = JaneDoe.getClass().getSuperclass().getDeclaredFields();
        Field email = fields[2]; 
        email.setAccessible(true);
        


         if (Modifier.isStatic(email.getModifiers())) {      
            
        try{
            email.setAccessible(true);

            email.set(JaneDoe, null);
    
        }
        catch (IllegalAccessException ill){
            System.out.println("Ошибка доступа: " + ill);
        }}


        JaneDoe.setDate_of_birth(null);
        JaneDoe.setStatus(null);
        JaneDoe.setAge(null);
        JaneDoe.setAdmined_subs(null);

        CURRENT.setUser(Cassady);
        assertEquals(service.logOut(CURRENT), "Вы успешно вышли из аккаунта!");
        assertEquals(CURRENT.getId(), JaneDoe.getId());
        assertEquals(CURRENT.isPassed(), JaneDoe.isPassed());
        assertEquals(CURRENT.getNickname(), JaneDoe.getNickname());
        assertEquals(CURRENT.getPassword(), JaneDoe.getPassword());
        assertEquals(CURRENT.getEmail(), JaneDoe.getEmail());
        assertEquals(CURRENT.getDate_of_birth(), JaneDoe.getDate_of_birth());
        assertEquals(CURRENT.getStatus(), JaneDoe.getStatus());
        assertEquals(CURRENT.getAge(), JaneDoe.getAge());
        assertEquals(CURRENT.getAdmined_subs(), JaneDoe.getAdmined_subs());
    }


    @Test
    void makeAdmin_notOk(){
        CURRENT.setUser(Bill);
        assertEquals(service.makeAdmin(3L, CURRENT), "У вас нет должности главного админа, чтобы сделать другого пользователя админом.");
    }

    @Test
    void makeAdmin_Ok(){
        User Will = new User();
	    Object[] Will_values = new Object[] {"Will", "FamilyGuy", "brthr@location.com", LocalDate.parse("1961-12-28"), 2};
        Will.setUser(4L, Will_values);
        CURRENT.setUser(Will);
        assertEquals(service.makeAdmin(3L, CURRENT), "Теперь Cassady администратор Бреддита!");
        assertEquals(Cassady.getStatus(), 1);
    }

}
