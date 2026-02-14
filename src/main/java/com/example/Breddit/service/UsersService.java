package com.example.Breddit.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.Breddit.controllers.PostController;
import com.example.Breddit.models.Code;
import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.CodeRepository;
import com.example.Breddit.repository.UserReposiotry;


public interface UsersService{
    List<User> findAllUsers();

    User saveUser(User user);

    User findUserbyId(Long id);

    User findUserbyEmail(String email);

    User updateUser(User user);

    boolean deleteUser(Long id);

    Code saveCode(Code code);

    Code updateCode(Code code);
    
    default Code generateCode(String email){
        Code code = new Code();
        code.generate(5);
        do {
            code.generate(5);
        } while (saveCode(code) == null);
        code.setUser_id(findUserbyEmail(email).getId());
        updateCode(code);
        return code;
    }
    

    
    @Async
    default Code TwoFactorsAuth(String email,JavaMailSender sender){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("hastur981@gmail.com");

        Code this_code = generateCode(email);
        message.setText(this_code.getValue());
        this_code.setActive(true);
        updateCode(this_code);

        message.setSubject("Breddit 2FA");
        sender.send(message);
        return this_code;
    }



    default String authUser(User user, CurrentUser CURRENT, User potetnial_user,JavaMailSender sender){

        if (potetnial_user == null) return "Такой почты не существует";
        
        String ur_password = user.getPassword();

        if (potetnial_user.getPassword().equals(ur_password) && ur_password != null){
            TwoFactorsAuth(user.getEmail(),sender);
            return "Мы выслали вам на почту проверочный код. Подтвердите, что это вы.";
        }
        return "Неверный пароль";
    };




    default boolean verification(User user, CurrentUser CURRENT, String ur_code,Code this_code){
        if (this_code != null && this_code.getUser_id().equals(user.getId())&& this_code.getActive()){
            CURRENT.setUser(user);
            this_code.setActive(false);
            updateCode(this_code);
            return true;  
        }
            
        return false;
    }



    default String logOut(CurrentUser CURRENT){
        try {
          CURRENT.logOut();
        return "Вы успешно вышли из аккаунта!";  
        }
        catch (Exception e){
            return "Что-то пошло не так: " + e;
        }
    }




    default String makeAdmin(Long id, CurrentUser CURRENT){
        if (CURRENT.getStatus().equals(2)){
            User new_admin = findUserbyId(id);
            new_admin.setStatus(1);
            updateUser(new_admin);
            return "".format("Теперь %s администратор Бреддита!", new_admin.getNickname());
        }

        return "У вас нет должности главного админа, чтобы сделать другого пользователя админом.";
    }
}
    

