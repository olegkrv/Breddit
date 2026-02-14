package com.example.PetProject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.Breddit.controllers.UserController;
import com.example.Breddit.models.Code;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.CodeRepository;
import com.example.Breddit.repository.PostReposiroty;
import com.example.Breddit.repository.SubRepository;
import com.example.Breddit.repository.UserReposiotry;
import com.example.Breddit.service.UsersService;
import com.example.Breddit.service.JPA.UsersServiceJPA;

import lombok.AllArgsConstructor;

@SpringBootTest
class UserControllerTest {
	private User Alex;
	private Object[] Alex_values = new Object[] {"Alex", "qwerty", "sample@example.com", "1987-03-11", -1};

    private User Bill;
	private Object[] Bill_values = new Object[] {"Bill", "JR", "sstr@location.com", "1957-12-28", 1};

    private User Cassady;
	private Object[] Cassady_values = new Object[] {"Cassady", "Fr3d", "jaw@bear.com", "1979-03-11", 0};

    @Mock
	private UsersService service;


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

        Mockito.when(service.findUserbyId(3L)).thenReturn(Cassady);
    }

    @Test
    void findById_notAdmin(){
        userController.CURRENT.setUser(Alex);
        assertEquals(userController.findUserbyId(3L), null);
    }

    @Test
    void findById_Admin(){
        userController.CURRENT.setUser(Bill);
        assertEquals(userController.findUserbyId(3L), Cassady);
    }

}
