package com.example.Breddit.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Breddit.models.Code;
import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.CodeRepository;
import com.example.Breddit.repository.PostReposiroty;
import com.example.Breddit.repository.SubRepository;
import com.example.Breddit.repository.UserReposiotry;
import com.example.Breddit.repository.Interfaces.UserPreRepository;
import com.example.Breddit.service.UsersService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public abstract class UserServiceImpl implements UsersService{
     public CurrentUser CURRENT = new CurrentUser();
    public boolean two_fa_passed = false;
    private final SubRepository sub_repository;
    private final CodeRepository code_repository;
    private UserPreRepository repository;
    private final PostReposiroty post_repository;


    @Autowired
    private final JavaMailSender sender;
    
    public List<User> findAllUsers(){
        try{return repository.findAll();}
        catch (Exception exception){System.out.println(exception); return null;}
        //return repository.findAll();
    }

    public String saveUser(User user){
        if (findUserbyEmail(user.getEmail()) == null){
           repository.save(user);
           TwoFactorsAuth(user.getEmail(),sender);
           return "Мы выслали вам на почту проверочный код. Подтвердите, что это вы.";}
        else  if (two_fa_passed) return "".format("Добро пожаловать, %s", CURRENT.getNickname());
        return "Эта почта уже используется!";
    }

    public User findUserbyEmail(String email){
        return repository.findUserByemail(email);
    }


    public User findUserbyId(Long id){
        return repository.findUserByid(id);
    }


    public User updateUser(User user){
        return repository.save(user);
    }

    @Transactional
    public boolean deleteUser(Long id)
    {
        if (repository.findUserByid(id) != null) {
            for (Long post: repository.findUserByid(id).getPosts()) {
                sub_repository.findByid(post_repository.findByid(post).getSub_id()).deletePost(post);
                post_repository.deleteByid(post);
            }
        
            repository.deleteByid(id); 
            return true;
        }
        return false;
    }


    public Code saveCode(Code code){
        if (code_repository.findByvalue(code.getValue()) != null) return null;
        return code_repository.save(code);
    }

    public Code updateCode(Code code){
        return code_repository.save(code);
    }



    //Ниже бывшие дефолты


    public Code generateCode(String email){
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
    public Code TwoFactorsAuth(String email,JavaMailSender sender){
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



    public String authUser(User user, User potetnial_user){
        
        if (potetnial_user == null) return "Такой почты не существует";
        
        String ur_password = user.getPassword();

        if (potetnial_user.getPassword().equals(ur_password) && ur_password != null){
            TwoFactorsAuth(user.getEmail(),sender);
            return "Мы выслали вам на почту проверочный код. Подтвердите, что это вы.";
        }
        return "Неверный пароль";
    };




    public boolean verification(User user, String ur_code,Code this_code){
        if (this_code != null && this_code.getUser_id().equals(user.getId())&& this_code.getActive()){
            CURRENT.setUser(user);
            this_code.setActive(false);
            updateCode(this_code);
            return true;  
        }
            
        return false;
    }



    public String logOut(){
        try {
          CURRENT.logOut();
        return "Вы успешно вышли из аккаунта!";  
        }
        catch (Exception e){
            return "Что-то пошло не так: " + e;
        }
    }




    public String makeAdmin(Long id){
        if (CURRENT.getStatus().equals(2)){
            User new_admin = findUserbyId(id);
            new_admin.setStatus(1);
            updateUser(new_admin);
            return "".format("Теперь %s администратор Бреддита!", new_admin.getNickname());
        }

        return "У вас нет должности главного админа, чтобы сделать другого пользователя админом.";
    }
}
