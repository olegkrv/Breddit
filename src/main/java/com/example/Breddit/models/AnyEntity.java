package com.example.Breddit.models;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AnyEntity {
    protected void setThis(Object[] fields) throws IllegalAccessError{
        Class<?> self = this.getClass();

        Field[] my_fields = self.getDeclaredFields(); 


        for (int i=0; i<fields.length; i++){ 
            Field my_field = my_fields[i];
            Object other_field = fields[i];
            System.out.println("my_field: " + my_field + "i: " + i);
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
                       ", но получил " + other_field.getClass().getSimpleName() + " от поля " + other_field);
    
        }
        
        catch (ArrayIndexOutOfBoundsException aio){
            System.err.println("fields: "+fields + " field: " + other_field + "i: " + i);
        }
    
    }
    Field my_field = null;
    Field other_field = null;
}
}
