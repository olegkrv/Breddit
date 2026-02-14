package com.example.Breddit.models;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Entity
@Table(name = "Breddit Users")

public class User extends UserTemplate{
    @Id
    @GeneratedValue
    private Long id;

    public User(){
        super();
    }
    
    public void setUser(Long id, Object[] fields) throws IllegalAccessError{
        setId(id);

        Class<?> self = this.getClass();

        Field[] my_fields = self.getSuperclass().getDeclaredFields(); 



        for (int i=0; i<my_fields.length; i++){ 
            Field my_field = my_fields[i];
            Object other_field = fields[i];
            if (Modifier.isStatic(my_field.getModifiers())) {
                    continue;
                }

            try {

            my_field.setAccessible(true);

            my_field.set(this, other_field);
        
        }
        catch (NoSuchFieldError nsf){
            System.out.println("Нет поля: " + my_field);
        }

        catch (IllegalAccessException ill){
            System.out.println("Ошибка доступа: " + ill);
        }
        catch (IllegalArgumentException iae){
            System.err.println("ОШИБКА ТИПОВ: " + my_field.getName() + 
                       " ожидал " + my_field.getType().getSimpleName() + 
                       ", но получил " + other_field.getClass().getSimpleName());
    
        }       
    
    }
    Field my_field = null;
    Field other_field = null;
}

} 
