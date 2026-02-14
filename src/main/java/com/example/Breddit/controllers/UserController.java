package com.example.Breddit.controllers;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Breddit.BredditApplication;
import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.CodeRepository;
import com.example.Breddit.repository.SubRepository;
import com.example.Breddit.service.UsersService;

import lombok.RequiredArgsConstructor;
//import com.example.Breddit.BredditApplication;;

@RestController
@RequestMapping("/br/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController{
    // @NonNull List<User>
    public CurrentUser CURRENT = new CurrentUser();
    public boolean two_fa_passed = false;
    private final UsersService service;
    private final SubRepository sub_repository;
    private final CodeRepository code_repository;

    @Autowired
    private final JavaMailSender sender;

    @GetMapping
    public List<User> findAllUsers(){
        return service.findAllUsers();
    }

    @PostMapping("/save_user")
    public String saveUser(@RequestBody User user){
        if (service.findUserbyEmail(user.getEmail()) == null){
            service.saveUser(user);
            service.TwoFactorsAuth(user.getEmail(),sender);
            return "Мы выслали вам на почту проверочный код. Подтвердите, что это вы.";}
        else  if (two_fa_passed) return "".format("Добро пожаловать, %s", CURRENT.getNickname());
        return "Эта почта уже используется!";
        
    }

    @GetMapping("/{id}")
    public User findUserbyId(@PathVariable Long id){
        if (CURRENT.getStatus().equals(1)) return service.findUserbyId(id);
        return null;
    }

    @PutMapping("/update_user")
    public User updateUser(@RequestBody User user){
       return service.updateUser(user);
    }

    @DeleteMapping("/delete_user")
    public String deleteUser(){
        System.out.println(CURRENT.getId() + "==" + !(CURRENT.getAdmined_subs().isEmpty()) + "==" + CURRENT.getAdmined_subs());
        if (CURRENT.getId() !=null && !(CURRENT.getAdmined_subs().isEmpty())) {
            
            String message = "Вы не уможете удалить свой аккаунт, так как являетесь главным админом в следующих Саббреддитах:";
            for (Long sub_id: CURRENT.getAdmined_subs()){
                message += "\n" + sub_repository.findByid(sub_id).getTitle();
            }
            message += "\n" +  "Передайте в данных Саббредитах права главного админа другому пользователю. ";
            return message;
        }
        else if (service.deleteUser(CURRENT.getId())){
            CURRENT.logOut();
            return "Пользователь успешно удалён.";
        }
        return "Вы не вошли в аккаунт, чтобы его удалять...";
    }

    @GetMapping("/current")
    public ArrayList<String> getCurrentUser(){return CURRENT.getUser();}

    @PostMapping("/authorization")
    public String authUser(@RequestBody User user){
        if (two_fa_passed) return "".format("Рады видеть вас снова, %s", CURRENT.getNickname());
        return service.authUser(user, CURRENT, service.findUserbyEmail(user.getEmail()), sender);
    }

    @PostMapping("/2fa/{code}")
    public String verification(@RequestBody User user, @PathVariable String code){
        if (CURRENT.getStatus().equals(-1)) return "Вы забанены.";
        two_fa_passed = service.verification(service.findUserbyEmail(user.getEmail()), CURRENT, code, code_repository.findByvalue(code));
        if (two_fa_passed){
            return "Успешно";
        }
        return "Неверный код!";
    }

    @GetMapping("/logout")
    public String logOut(){
        two_fa_passed = false;
        return service.logOut(CURRENT);
    }


    @PostMapping("/make_admin/{id}")
    public String makeAdmin(@PathVariable Long id){
        return service.makeAdmin(id, CURRENT);
    }
}
