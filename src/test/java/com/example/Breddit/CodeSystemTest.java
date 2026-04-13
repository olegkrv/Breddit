package com.example.Breddit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.Breddit.controllers.UserController;
import com.example.Breddit.models.Code;
import com.example.Breddit.models.Sub;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.CodeRepository;
import com.example.Breddit.repository.SubRepository;
import com.example.Breddit.repository.UserReposiotry;
import com.example.Breddit.repository.JPA.UserJPA;
import com.example.Breddit.service.UsersService;
import com.example.Breddit.service.Impl.UserServiceImpl;

@DataJpaTest
@EntityScan(basePackages = "com.example.Breddit.models")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CodeSystemTest {
    private User Alex;
	private Object[] Alex_values = new Object[] {"Alex", "qwerty", "sample@example.com", LocalDate.parse("1987-03-11"), -1};

    private User Bill;
	private Object[] Bill_values = new Object[] {"Bill", "JR", "sstr@location.com", LocalDate.parse("1957-12-28"), 1};

    private User Cassady;
	private Object[] Cassady_values = new Object[] {"Cassady", "Fr3d", "jaw@bear.com", LocalDate.parse("1979-03-11"), 0};

    private User NonRegistredUser;
	private Object[] NRU_values = new Object[] {"void", "nothingness", "idk@what.com", LocalDate.parse("1971-11-24"), 0};

    private Sub coolSub = new Sub();
    

    @Autowired
    private CodeRepository codeRepository;

    @Mock
    JavaMailSender sender;

    @Autowired
    private UserReposiotry true_repo;

    private UserJPA userRepository;
    private UsersService userService;
    private UserController userController;

	@BeforeEach
	void setUp() throws Exception{
        this.userRepository = new UserJPA(true_repo);
        this.userService = new UserServiceImpl(userRepository, codeRepository, null, null, sender);
        this.userController = new UserController(userService, null, codeRepository, sender);
        

		this.Alex = new User();
        Alex.setUser(null, Alex_values);

        this.Bill = new User();
        Bill.setUser(null, Bill_values);

        this.Cassady = new User();
        Cassady.setUser(null, Cassady_values);
    }

    @Test
    void generateCode(){
        Code code = userService.generateCode("jaw@bear.com");
        assertEquals(code, codeRepository.findByvalue(code.getValue()));
        assertEquals(code.getUemail(), "jaw@bear.com");
    }

    @Test
    void authUserNotExist(){
        assertEquals(userController.authUser(Cassady), "Такой почты не существует");
    }

    @Test
    void authUserWrongPassword(){
        userRepository.save(Cassady);
        
        User Cassady1 = new User();
        Cassady1.setUser(null, Cassady_values);
        Cassady1.setPassword("wrongPassword");
        assertEquals(userController.authUser(Cassady1), "Неверный пароль");
    }

    @Test
    void authUserWrongCode(){
        userRepository.save(Cassady);
        assertEquals(userController.authUser(Cassady), "Мы выслали вам на почту проверочный код. Подтвердите, что это вы.");

        Code trueCode = codeRepository.findAll().get(0);
        assertEquals(userController.verification(Cassady, trueCode.getValue() + "1"), "Неверный код!");
    }

    @Test
    void authUserOk(){
        userRepository.save(Cassady);
        assertEquals(userController.authUser(Cassady), "Мы выслали вам на почту проверочный код. Подтвердите, что это вы.");

        Code trueCode = codeRepository.findAll().get(0);
        assertEquals(userController.verification(Cassady, trueCode.getValue()), "Успешно");

        assertEquals(userController.authUser(Cassady), "Рады видеть вас снова, Cassady");

        assertEquals(userController.CURRENT.getId(), userRepository.findUserByemail("jaw@bear.com").getId());
    }
}
