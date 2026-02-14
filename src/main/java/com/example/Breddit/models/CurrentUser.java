package com.example.Breddit.models;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

//@Entity
@Setter
@Getter
public class CurrentUser extends UserTemplate{
    private Long id;
    public CurrentUser(){super();}

    public void setUser(User user) throws IllegalAccessError{
        setId(user.getId());

        Class<?> this_user = user.getClass();
        Class<?> self = this.getClass();

        Field[] my_fields = self.getSuperclass().getDeclaredFields(); 

        Field[] fields = this_user.getSuperclass().getDeclaredFields();


        for (int i=0; i<fields.length; i++){ 
            Field my_field = my_fields[i];
            Field other_field = fields[i];

            if (Modifier.isStatic(other_field.getModifiers())) {
                    continue;
                }
            
            try {

            other_field.setAccessible(true);

            other_field.set(this, other_field.get(user));
        
        }
        catch (NoSuchFieldError nsf){
            System.out.println("Нет поля: " + my_field);
        }

        catch (IllegalAccessException ill){
            System.out.println("Ошибка доступа: " + ill);
        }
    
    }
    Field my_field = null;
    Field other_field = null;
}
    

    public void logOut(){
        setId(null);

        Class<?> self = this.getClass();

        Field[] my_fields = self.getSuperclass().getDeclaredFields(); 


        for (int i=0; i<my_fields.length; i++){ 
            Field my_field = my_fields[i];
            if (Modifier.isStatic(my_field.getModifiers())) {
                    continue;
                }
            
            try {
              my_field.setAccessible(true);
              my_field.set(this, null);
            }

        catch (NoSuchFieldError nsf){
            System.out.println("Нет поля: " + my_field);
        }

        catch (IllegalAccessException ill){
            
            System.out.println("Ошибка доступа: " + ill);
        }
    }}


    public ArrayList<String> getUser() throws NoSuchFieldError{
        
        Class<?> self = this.getClass();

        Field[] my_fields = self.getSuperclass().getDeclaredFields(); 

        ArrayList<String> data = new ArrayList<>();
        data.add("".format("id: %s", getId())); 

        for (int i=0; i<my_fields.length; i++){ 
            Field my_field = my_fields[i];
            if (Modifier.isStatic(my_field.getModifiers())) {
                    continue;
                }
            
            try {
              my_field.setAccessible(true);
            data.add("".format("%s: %s", my_field.getName(), my_field.get(this)));
            }

        catch (NoSuchFieldError nsf){
            System.out.println("Нет поля: " + my_field);
        }

        catch (IllegalAccessException ill){
            System.out.println("Ошибка доступа: " + ill);
        }
    }
        return data;
    }


    
}

