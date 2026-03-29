package com.example.Breddit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.ArrayList;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.Breddit.controllers.UserController;
import com.example.Breddit.models.Code;
import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.Sub;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.CodeRepository;
import com.example.Breddit.repository.PostReposiroty;
import com.example.Breddit.repository.SubRepository;
import com.example.Breddit.repository.UserReposiotry;
import com.example.Breddit.service.UsersService;
import com.example.Breddit.service.Impl.UserServiceImpl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	private User Alex;
	private Object[] Alex_values = new Object[] {"Alex", "qwerty", "sample@example.com", LocalDate.parse("1987-03-11"), -1};

    private User Bill;
	private Object[] Bill_values = new Object[] {"Bill", "JR", "sstr@location.com", LocalDate.parse("1957-12-28"), 1};

    private User Cassady;
	private Object[] Cassady_values = new Object[] {"Cassady", "Fr3d", "jaw@bear.com", LocalDate.parse("1979-03-11"), 0};

    private User NonRegistredUser;
	private Object[] NRU_values = new Object[] {"void", "nothingness", "idk@what.com", LocalDate.parse("1971-11-24"), 0};

    private Sub coolSub = new Sub();

    ArrayList<User> USERS = new ArrayList<>();

    @Mock
    private SubRepository sub_repository;

    @Mock
	private UsersService service;


    @Mock
    private CodeRepository code_repository;

    private Code goodCode = new Code("12345");


	@InjectMocks
    private UserController userController;

	@BeforeEach
	void setUp() throws Exception{
		this.Alex = new User();
        Alex.setUser(1L, Alex_values);

        this.Bill = new User();
        Bill.setUser(2L, Bill_values);

        this.Cassady = new User();
        Cassady.setUser(3L, Cassady_values);

        this.NonRegistredUser = new User();
        NonRegistredUser.setUser(null, NRU_values);

        USERS.add(Alex);
        USERS.add(Bill);
        USERS.add(Cassady);

        Mockito.when(service.findAllUsers()).thenReturn(USERS);

        coolSub.setId(1l);
        coolSub.setTitle("cuteCats");
        coolSub.setDescription("smthng");

        Mockito.when(service.findUserbyId(3L)).thenReturn(Cassady);
        Mockito.when(service.saveUser(Cassady)).thenReturn(Cassady);

        Mockito.when(service.findUserbyEmail("jaw@bear.com")).thenReturn(Cassady);
        Mockito.when(service.findUserbyEmail("idk@what.com")).thenReturn(null);

        Mockito.when(sub_repository.findByid(1L)).thenReturn(coolSub);

        Mockito.when(service.deleteUser(3L)).thenReturn(true);


        Mockito.when(service.verification(Cassady, "12345", goodCode, userController.CURRENT)).thenReturn(true);

        Mockito.when(code_repository.findByvalue("12345")).thenReturn(goodCode);
    }


    @Test
    void findAll_Banned(){
        userController.CURRENT.setUser(Alex);
        assertEquals(userController.findAllUsers(), null);
    }

    @Test
    void findAll_notAdmin(){
        userController.CURRENT.setUser(Cassady);
        assertEquals(userController.findAllUsers(), null);
    }

    @Test
    void findAll_Admin(){
       userController.CURRENT.setUser(Bill);
       assertEquals(userController.findAllUsers(), USERS);
    }

    @Test
    void findAll_MainAdmin(){
        User Will = new User();
	    Object[] Will_values = new Object[] {"Will", "FamilyGuy", "brthr@location.com", LocalDate.parse("1961-12-28"), 2};
        Will.setUser(4L, Will_values);
        userController.CURRENT.setUser(Will);
        assertEquals(userController.findAllUsers(), USERS);
    }

    @Test
    void findById_banned(){
        userController.CURRENT.setUser(Alex);
        assertEquals(userController.findUserbyId(3L), null);
    }

    @Test
    void findById_notAdmin(){
        userController.CURRENT.setUser(Cassady);
        assertEquals(userController.findUserbyId(3L), null);
    }

    @Test
    void findById_Admin(){
        userController.CURRENT.setUser(Bill);
        assertEquals(userController.findUserbyId(3L), Cassady);
    }

    @Test
    void findById_MainAdmin(){
        User Will = new User();
	    Object[] Will_values = new Object[] {"Will", "FamilyGuy", "brthr@location.com", LocalDate.parse("1961-12-28"), 2};
        Will.setUser(4L, Will_values);
        userController.CURRENT.setUser(Will);
        assertEquals(userController.findUserbyId(3L), Cassady);
    }

    @Test
    void saveUser_notOk(){
        assertEquals(userController.saveUser(Cassady), "Эта почта уже используется!");
    }

    @Test
    void saveUser_Ok(){
        assertEquals(userController.saveUser(NonRegistredUser),"Мы выслали вам на почту проверочный код. Подтвердите, что это вы.");
        userController.CURRENT.setUser(NonRegistredUser);
        userController.CURRENT.setPassed(true);
        Mockito.when(service.findUserbyEmail("idk@what.com")).thenReturn(NonRegistredUser);
        assertEquals(userController.saveUser(NonRegistredUser),"Добро пожаловать, void");
        
    }

    @Test
    void deleteSub_when_you_are_admin(){
        Cassady.addAdminedSub(1L);
        userController.CURRENT.setUser(Cassady);
        String message = "Вы не уможете удалить свой аккаунт, так как являетесь главным админом в следующих Саббреддитах:" + "\n" + "cuteCats";
        message += "\n" +  "Передайте в данных Саббредитах права главного админа другому пользователю. ";
        assertEquals(userController.deleteUser(), message);
    }

    @Test
    void deleteSub_when_you_are_not_admin(){
        userController.CURRENT.setUser(Cassady);
        assertEquals(userController.deleteUser(), "Пользователь успешно удалён.");
    }

    @Test
    void deleteSub_null_current(){
        assertEquals(userController.deleteUser(), "Вы не вошли в аккаунт, чтобы его удалять...");
    }


    /*@Test
    void vereficationBanned(){
        assertEquals(userController.verification(Alex, "12345"), "Вы забанены.");
    }
        
    успешно, но функция не имела смысла
    */
    
    @Test
    void verificationWrong(){
        assertEquals(userController.verification(Cassady, "121212"), "Неверный код!");
    }

    @Test
    void verificationGood(){
        assertEquals(userController.verification(Cassady, "12345"), "Успешно");
    }
    

}
