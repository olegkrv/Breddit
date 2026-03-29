package com.example.Breddit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.Rollback;

import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.Sub;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.CodeRepository;
import com.example.Breddit.repository.UserReposiotry;
import com.example.Breddit.repository.Interfaces.PostPreRepository;
import com.example.Breddit.repository.Interfaces.SubPreRepository;
import com.example.Breddit.repository.Interfaces.UserPreRepository;
import com.example.Breddit.repository.JPA.UserJPA;
import com.example.Breddit.service.UsersService;
import com.example.Breddit.service.Impl.UserServiceImpl;

import lombok.AllArgsConstructor;

@DataJpaTest
//EnableJpaRepositories(basePackages = "java.com.example.Breddit.repository")
@EntityScan(basePackages = "com.example.Breddit.models")
//@EnableJpaRepositories(basePackageClasses = UserJPA.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    private User Alex;
	private Object[] Alex_values = new Object[] {"Alex", "qwerty", "sample@example.com", LocalDate.parse("1987-03-11"), -1};

    private User Bill;
	private Object[] Bill_values = new Object[] {"Bill", "JR", "sstr@location.com", LocalDate.parse("1957-12-28"), 1};

    private User Cassady;
	private Object[] Cassady_values = new Object[] {"Cassady", "Fr3d", "jaw@bear.com", LocalDate.parse("1979-03-11"), 0};

    private User NonRegistredUser;
	private Object[] NRU_values = new Object[] {"void", "nothingness", "idk@what.com", LocalDate.parse("1971-11-24"), 0};

    @Autowired
    private UserReposiotry true_repo;

    private UserJPA repository;

    //@Autowired
    //private CodeRepository codeRepository;

    //@Autowired

    @BeforeEach
    void setUp() throws Exception{
        this.repository = new UserJPA(true_repo);

        this.Alex = new User();
        Alex.setUser(null, Alex_values);

        this.Bill = new User();
        Bill.setUser(null, Bill_values);

        this.Cassady = new User();
        Cassady.setUser(null, Cassady_values);

        this.NonRegistredUser = new User();
        NonRegistredUser.setUser(null, NRU_values);
    }


    @Test
    void findAll(){
        assertEquals(repository.save(Alex), Alex);
        assertEquals(repository.save(Bill), Bill);
        assertEquals(repository.save(Cassady), Cassady);

        ArrayList<User> USERS = new ArrayList<>();
        Alex.setId(1L);
        Bill.setId(2L);
        Cassady.setId(3L);

        USERS.add(Alex);
        USERS.add(Bill);
        USERS.add(Cassady);

        assertEquals(USERS, repository.findAll());
    }

    @Test
    void save(){
        assertEquals(repository.save(Alex), Alex);
        assertEquals(repository.save(Bill), Bill);
        assertEquals(repository.save(Cassady), Cassady);
    }

    @Test
    void findUserByEmail(){
        assertEquals(repository.save(Alex), Alex);
        assertEquals(repository.save(Bill), Bill);
        assertEquals(repository.save(Cassady), Cassady);

        assertEquals(repository.findUserByemail("sample@example.com"), Alex);
        assertEquals(repository.findUserByemail("sstr@location.com"), Bill);
        assertEquals(repository.findUserByemail("jaw@bear.com"), Cassady);
        assertEquals(repository.findUserByemail("idk@what.com"), null);
    }

    @Test
    void findUserById(){
        assertEquals(repository.save(Alex), Alex);
        assertEquals(repository.save(Bill), Bill);
        assertEquals(repository.save(Cassady), Cassady);

        assertEquals(repository.findUserByid(1L), Alex);
        assertEquals(repository.findUserByid(2L), Bill);
        assertEquals(repository.findUserByid(3L), Cassady);
        assertEquals(repository.findUserByid(4L), null);
    }

    @Test
    void deleteUserById(){
        assertEquals(repository.save(Alex), Alex);
        assertEquals(repository.save(Bill), Bill);
        assertEquals(repository.save(Cassady), Cassady);

        assertEquals(repository.findUserByid(1L), Alex);
        assertEquals(repository.findUserByid(2L), Bill);
        assertEquals(repository.findUserByid(3L), Cassady);

        repository.deleteByid(1L);
        repository.deleteByid(2L);
        repository.deleteByid(3L);

        assertEquals(repository.findUserByid(1L), null);
        assertEquals(repository.findUserByid(2L), null);
        assertEquals(repository.findUserByid(3L), null);
    }
}
