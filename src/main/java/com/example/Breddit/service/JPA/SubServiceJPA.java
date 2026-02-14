package com.example.Breddit.service.JPA;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.Breddit.controllers.UserController;
import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.Post;
import com.example.Breddit.models.Sub;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.PostReposiroty;
import com.example.Breddit.repository.SubRepository;
import com.example.Breddit.service.SubService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Primary
public class SubServiceJPA implements SubService{
    private final SubRepository repository; 
    private final PostReposiroty post_repository;
    private final UserController user_controller;

    @Override
    public List<Sub> findAllSubs(){
        return repository.findAll();
    }

    @Override
    public Sub addSub(Sub sub){
        try {
            Sub new_sub = repository.save(sub);
            Long new_main_admin = user_controller.CURRENT.getId();

            new_sub.setMain_admin(new_main_admin);
            User admining = user_controller.findUserbyId(new_main_admin);
            admining.addAdminedSub(new_sub.getId());
            user_controller.CURRENT.addAdminedSub(new_sub.getId());
            user_controller.updateUser(admining);
            repository.save(new_sub);

            new_main_admin = null;
            admining = null;
            System.out.println(new_sub);
            return new_sub;
        }
        catch (Exception exception) {
            System.out.println(exception);
            return null;}
    }

    @Override
    public Sub fullUpdate(Sub sub){
        return repository.save(sub);
    }

    @Override
    public Sub updateSub(Long id, Map<String, String> updating){
        Sub sub = repository.findByid(id);
        Class<?> clazz = sub.getClass();
       
        for (String key : updating.keySet()) {
            
            try {

                Field field = clazz.getDeclaredField(key); 

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;}
                field.setAccessible(true);

                 
                field.set(sub, updating.get(key));
                field = null;
                
                
            }
            
            catch (NoSuchFieldException nsf){
                System.out.println("Нет поля: " + key);
            }

            catch (IllegalAccessException ill){
                System.out.println("Ошибка доступа: " + ill);
            }
        }
        System.out.println(sub);
        return repository.save(sub);
    }

    

    @Override
    public Sub findByTitle(String title){
        return repository.findBytitle(title);
    }

    @Override
    public Sub findById(Long id){
        return repository.findByid(id);
    }

    @Override
    @Transactional
    public boolean deleteSub(Long id){
        if (repository.findByid(id) != null) {
            for (Long post: repository.findByid(id).getPosts()) {
                user_controller.findUserbyId(post_repository.findByid(post).getAuthor()).deletePost(post);
                post_repository.deleteByid(post);}
            
            User main_admin = user_controller.findUserbyId(repository.findByid(id).getMain_admin());
            main_admin.deleteAdminedSub(id);
            user_controller.updateUser(main_admin);
            user_controller.CURRENT.deleteAdminedSub(id);
            repository.deleteByid(id); 
            return true;
        }
        return false;
    }

    @Override
    public void addAdmin(Long sub_id, Long user_id){
        Sub this_sub = repository.findByid(sub_id);
        ArrayList<Long> admins = this_sub.getAdmins();
        admins.add(user_id);
        this_sub.setAdmins(admins);
        repository.save(this_sub);
        this_sub = null;
    }

    @Override
    public void removeAdmin(Long sub_id, Long user_id){
        Sub this_sub = repository.findByid(sub_id);
        ArrayList<Long> admins = this_sub.getAdmins();
        admins.remove(user_id);
        this_sub.setAdmins(admins);
        repository.save(this_sub);
        this_sub = null;
    }

    @Override
    public void transferCrown(Long sub_id, Long user_id){
        Sub this_sub = repository.findByid(sub_id);
        this_sub.setMain_admin(user_id);

        
        user_controller.CURRENT.deleteAdminedSub(sub_id);
        User current = user_controller.findUserbyId(user_controller.CURRENT.getId());
        current.deleteAdminedSub(sub_id);
        user_controller.updateUser(current);

        User new_main_admin = user_controller.findUserbyId(user_id);
        new_main_admin.addAdminedSub(sub_id);
        user_controller.updateUser(new_main_admin);

        repository.save(this_sub);
        this_sub = null;
        new_main_admin = null;
    }
}
