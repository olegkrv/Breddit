package com.example.Breddit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.mapping.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.Breddit.controllers.PostController;
import com.example.Breddit.controllers.SubController;
import com.example.Breddit.controllers.UserController;
import com.example.Breddit.models.Sub;
import com.example.Breddit.models.User;
import com.example.Breddit.service.PostService;
import com.example.Breddit.service.SubService;
import com.example.Breddit.service.UsersService;

@SpringBootTest
public class SubControllerTest {
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
    private Sub bossFight = new Sub();

    @Mock 
    private SubService service;

    @Mock
    private UsersService user_service;

    @Spy
    private UserController userController = new UserController(user_service, null, null, null);

	@InjectMocks
    private SubController subController;


    

	@BeforeEach
	void setUp() throws Exception{
        this.Alex = new User();
        Alex.setUser(1L, Alex_values);
        Mockito.when(user_service.findUserbyId(1L)).thenReturn(Alex);

        this.Bill = new User();
        Bill.setUser(2L, Bill_values);
        Mockito.when(user_service.findUserbyId(2L)).thenReturn(Bill);

        this.Cassady = new User();
        Cassady.setUser(3L, Cassady_values);
        Mockito.when(user_service.findUserbyId(3L)).thenReturn(Cassady);

        this.Will = new User();
        Will.setUser(4L, Will_values);
        Mockito.when(user_service.findUserbyId(4L)).thenReturn(Will);

        this.NonRegistredUser = new User();
        NonRegistredUser.setUser(null, NRU_values);

        coolSub.setId(1l);
        coolSub.setTitle("cuteCats");
        coolSub.setDescription("smthng");
        Mockito.when(service.findByTitle("cuteCats")).thenReturn(coolSub);
        Mockito.when(service.findById(1L)).thenReturn(coolSub);
        Mockito.when((service.addSub(bossFight))).thenReturn(bossFight);

        bossFight.setTitle("bossfight");
        bossFight.setDescription("Home all of your bosses.");
    }

    @Test
    void addSub_notAuth(){
        assertEquals(subController.addSub(bossFight), "Необходимо войти в аккаунт, чтобы создать саббреддит!");
    }

    @Test
    void addSub_Banned(){
        userController.CURRENT.setUser(Alex);
        assertEquals(subController.addSub(bossFight), "Вы забанены.");
    }

    @Test
    void addSub_EmptyTitle(){
        userController.CURRENT.setUser(Cassady);
        bossFight.setTitle(null);
        assertEquals(subController.addSub(bossFight), "Название саббреддита не может быть пустым!");
    }
    
    @Test
    void addSub_EmptyTitle2(){
        userController.CURRENT.setUser(Cassady);
        bossFight.setTitle("");
        assertEquals(subController.addSub(bossFight), "Название саббреддита не может быть пустым!");
    }

    @Test
    void addSub_EmptyTitle3(){
        userController.CURRENT.setUser(Cassady);
        bossFight.setTitle("              ");
        assertEquals(subController.addSub(bossFight), "Название саббреддита не может быть пустым!");
    }

    @Test
    void addSub_nonLatin(){
        userController.CURRENT.setUser(Cassady);
        bossFight.setTitle("Битва с начальником");
        assertEquals(subController.addSub(bossFight), "Можно использовать только латинские буквы!");
    }

    @Test
    void addSub_nonLatin2(){
        userController.CURRENT.setUser(Cassady);
        bossFight.setTitle("Битва с начальником: russian version");
        assertEquals(subController.addSub(bossFight), "Можно использовать только латинские буквы!");
    }

    @Test
    void addSub_alreadyExist(){
        userController.CURRENT.setUser(Cassady);
        assertEquals(subController.addSub(coolSub), "Саббреддит cuteCats уже есть в базе данных!");
    }

    @Test
    void addSub_Ok(){
        userController.CURRENT.setUser(Cassady);
        assertEquals(subController.addSub(bossFight), "Саббреддит успешно добавлен!");
    }


    @Test
    void updateSub_notAuth(){
        HashMap<String, String> updating = new HashMap<>();
        updating.put("title", "veryCuteCats");
        assertEquals(subController.updateSub(1L, updating), "Необходимо войти в аккаунт, чтобы обновить саббреддит!");
    }

    @Test
    void updateSub_Banned(){
        HashMap<String, String> updating = new HashMap<>();
        updating.put("title", "veryCuteCats");
        userController.CURRENT.setUser(Alex);
        assertEquals(subController.updateSub(1L, updating), "Вы забанены.");
    }

    @Test
    void updateSub_notExist(){
        HashMap<String, String> updating = new HashMap<>();
        updating.put("title", "veryCuteCats");
        userController.CURRENT.setUser(Cassady);
        assertEquals(subController.updateSub(2L, updating), "Саббреддита с таким id не существует!");
    }

    @Test
    void updateSub_notAdmin(){
        HashMap<String, String> updating = new HashMap<>();
        updating.put("title", "veryCuteCats");
        userController.CURRENT.setUser(Cassady);
        assertEquals(subController.updateSub(1L, updating), "У вас нет прав администратора, чтобы обновить данный Саббреддит!");
    }

    @Test
    void updateSub_Ok(){
        HashMap<String, String> updating = new HashMap<>();
        updating.put("title", "veryCuteCats");
        userController.CURRENT.setUser(Cassady);
        ArrayList<Long> Cassady_admin = new ArrayList<>();
        Cassady_admin.add(3L);
        bossFight.setAdmins(Cassady_admin);

        Mockito.when(service.findById(2L)).thenReturn(bossFight);
        assertEquals(subController.updateSub(2L, updating), "Саббреддит успешно обновлён!");
    }

    @Test
    void findByTitle_Banned(){
        userController.CURRENT.setUser(Alex);
        assertEquals(subController.findByTitle("cuteCats"), null);
    }

    @Test
    void findByTitle_Ok(){
        userController.CURRENT.setUser(Cassady);
        assertEquals(subController.findByTitle("cuteCats"), coolSub);
    }

    @Test
    void deleteSub_notAuth(){
        assertEquals(subController.deleteSub(1L), "Необходимо войти в аккаунт, чтобы удалить саббреддит!");
    }

    @Test
    void deleteSub_Banned(){
        userController.CURRENT.setUser(Alex);
        assertEquals(subController.deleteSub(1L), "Вы забанены.");
    }

    @Test
    void deleteSub_notExist(){
        userController.CURRENT.setUser(Cassady);
        assertEquals(subController.deleteSub(2L), "Саббреддита с таким id не существует!");
    }

    @Test
    void deleteSub_notAdmin(){
        userController.CURRENT.setUser(Cassady);
        assertEquals(subController.deleteSub(1L), "У вас нет прав главного администратора, чтобы удалить данный Саббреддит!");
    }

    @Test
    void deleteSub_Ok(){
        userController.CURRENT.setUser(Cassady);
        coolSub.setMain_admin(3L);
        assertEquals(subController.deleteSub(1L), "Саббреддит успешно удалён!");
    }

    @Test
    void addAdmin_notAuth(){
        assertEquals(subController.addAdmin("cuteCats", 2L), "Необходимо войти в аккаунт, чтобы добавить нового админа Саббреддита!");
    }

    @Test
    void addAdmin_Banned(){
        userController.CURRENT.setUser(Alex);
        assertEquals(subController.addAdmin("cuteCats", 2L), "Вы забанены.");
    }

    @Test
    void addAdmin_notAdmin(){
        userController.CURRENT.setUser(Cassady);
        assertEquals(subController.addAdmin("cuteCats", 2L), "У вас нет прав главного администратора, чтобы добавить нового админа Саббреддита!");
    }

    @Test
    void addAdmin_notExist(){
        userController.CURRENT.setUser(Cassady);
        coolSub.setMain_admin(3L);
        assertEquals(subController.addAdmin("cuteCats", 5L), "Пользователя с таким id не существует!");
    }

    @Test
    void addAdmin_Ok(){
        userController.CURRENT.setUser(Cassady);
        coolSub.setMain_admin(3L);
        assertEquals(subController.addAdmin("cuteCats", 2L), "Теперь Bill часть команды.");
    }

    @Test
    void removeAdmin_notAuth(){
        assertEquals(subController.removeAdmin("cuteCats", 2L), "Необходимо войти в аккаунт, чтобы удалить админа Саббреддита!");
    }

    @Test
    void removeAdmin_Banned(){
        userController.CURRENT.setUser(Alex);
        assertEquals(subController.removeAdmin("cuteCats", 2L), "Вы забанены.");
    }

    

    @Test
    void removeAdmin_notExist(){
        userController.CURRENT.setUser(Cassady);
        coolSub.setMain_admin(3L);
        assertEquals(subController.removeAdmin("cuteCats", 5L), "Пользователя с таким id не существует!");
    }

    @Test
    void removeAdmin_neverBeen(){
        userController.CURRENT.setUser(Cassady);
        coolSub.setMain_admin(3L);
        assertEquals(subController.removeAdmin("cuteCats", 2L), "Не знал этот Саббредит таких админов, как Bill.");
    }

    @Test
    void removeAdmin_notAdmin(){
        userController.CURRENT.setUser(Cassady);
        ArrayList<Long> admins = new ArrayList<>();
        admins.add(2L);
        coolSub.setAdmins(admins);
        assertEquals(subController.removeAdmin("cuteCats", 2L), "У вас нет прав главного администратора, чтобы удалить админа Саббреддита!");
    }

    @Test
    void removeAdmin_Ok(){
        userController.CURRENT.setUser(Cassady);
        coolSub.setMain_admin(3L);
        ArrayList<Long> admins = new ArrayList<>();
        admins.add(2L);
        coolSub.setAdmins(admins);
        assertEquals(subController.removeAdmin("cuteCats", 2L), "Теперь Bill изгнан из вышего уютного уголка.");
    }

    @Test
    void transferCrown_notAuth(){
        assertEquals(subController.transferCrown("cuteCats", 2L), "Необходимо войти в аккаунт, чтобы передать права главного админа Саббреддита!");
    }

    @Test
    void transferCrown_Banned(){
        userController.CURRENT.setUser(Alex);
        assertEquals(subController.transferCrown("cuteCats", 2L), "Вы забанены.");
    }

    @Test
    void transferCrown_notAdmin(){
        userController.CURRENT.setUser(Cassady);
        assertEquals(subController.transferCrown("cuteCats", 2L), "У вас нет прав главного администратора, чтобы передать права на этот Саббреддит!");
    }

    @Test
    void transferCrown_notExist(){
        userController.CURRENT.setUser(Cassady);
        coolSub.setMain_admin(3L);
        assertEquals(subController.transferCrown("cuteCats", 5L), "Пользователя с таким id не существует!");
    }

    @Test
    void transferCrownn_Ok(){
        userController.CURRENT.setUser(Cassady);
        coolSub.setMain_admin(3L);
        assertEquals(subController.transferCrown("cuteCats", 2L), "Теперь Bill хозяин сея Саббреддита.");
    }
}
