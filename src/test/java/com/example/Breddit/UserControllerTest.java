package com.example.Breddit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;

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
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.Breddit.controllers.UserController;
import com.example.Breddit.models.Code;
import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.CodeRepository;
import com.example.Breddit.repository.PostReposiroty;
import com.example.Breddit.repository.SubRepository;
import com.example.Breddit.repository.UserReposiotry;
import com.example.Breddit.service.UsersService;
import com.example.Breddit.service.Impl.UserServiceImpl;

import lombok.AllArgsConstructor;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	//@Mock
    private User Alex;
	private Object[] Alex_values = new Object[] {"Alex", "qwerty", "sample@example.com", LocalDate.parse("1987-03-11"), -1};

    //@Mock
    private User Bill;
	private Object[] Bill_values = new Object[] {"Bill", "JR", "sstr@location.com", LocalDate.parse("1957-12-28"), 1};

    //@Mock
    private User Cassady;
	private Object[] Cassady_values = new Object[] {"Cassady", "Fr3d", "jaw@bear.com", LocalDate.parse("1979-03-11"), 0};

    private User NonRegistredUser;
	private Object[] NRU_values = new Object[] {"void", "nothingness", "idk@what.com", LocalDate.parse("1971-11-24"), 0};

    @Mock
	private UsersService service;


    User user = Mockito.mock(User.class);

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


        Mockito.when(service.findUserbyId(3L)).thenReturn(Cassady);
        Mockito.when(service.saveUser(Cassady)).thenReturn(Cassady);

        Mockito.when(service.findUserbyEmail("jaw@bear.com")).thenReturn(Cassady);
        Mockito.when(service.findUserbyEmail("idk@what.com")).thenReturn(null);
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
    void saveUser_notOk(){
        assertEquals(userController.saveUser(Cassady), "Эта почта уже используется!");
    }

    @Test
    void saveUser_wrongCode(){
        assertEquals(userController.saveUser(NonRegistredUser),"Мы выслали вам на почту проверочный код. Подтвердите, что это вы.");
        userController.two_fa_passed = false;
        
    }


}
