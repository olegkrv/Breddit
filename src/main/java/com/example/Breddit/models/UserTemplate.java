package com.example.Breddit.models;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import com.example.Breddit.service.UsersService;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import java.lang.reflect.Modifier;

@MappedSuperclass
@Getter
@Setter
public abstract class UserTemplate extends OperationsWithPost{
 
    private String nickname;
    private String password;
    @NonNull
    @Column(unique = true)
    private String email;

    private LocalDate date_of_birth;
    
    @Transient
    private Integer status;

    @Transient
    private Integer age;

    

    public int getAge() {
        return Period.between(date_of_birth, LocalDate.now()).getYears();
     }
    
    private ArrayList<Long> admined_subs = new ArrayList<Long>();
    

    public void addAdminedSub(Long id){
        ArrayList<Long> my_subs = this.getAdmined_subs();
        System.out.println(my_subs);
        my_subs.add(id);
        System.out.println(id);
        System.out.println(my_subs);
        this.setAdmined_subs(my_subs);
        System.out.println(this.getAdmined_subs());
        my_subs = null;
    }
    
    public void deleteAdminedSub(Long id){
        ArrayList<Long> my_subs = this.getAdmined_subs();
        my_subs.remove(id);
        this.setAdmined_subs(my_subs);
        my_subs = null;
    }

}

    

