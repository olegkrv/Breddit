package com.example.Breddit.models;

import java.security.SecureRandom;

import com.example.Breddit.repository.CodeRepository;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "2FA_codes")
public class Code {
    public Code(){
        this.id = null;
        this.user_id = null;
    }

    @Id
    @GeneratedValue
    private final Long id;
    private Long user_id;
    private boolean active = false;

    public boolean getActive(){
        return this.active;
    }

    private final static SecureRandom random = new SecureRandom();
    private static final String ALPHANUM = "ABCDEFGHJKLMNPQRSTUVWXYZ0123456789";

    private String value;
    
    public void generate(int length){
        String will_be = "";
        for (int i=0; i<length; i++) will_be += ALPHANUM.charAt(random.nextInt(ALPHANUM.length()));
        this.setValue(will_be);
        will_be = null;
    }


    public void customGenerate(String value){
        this.setValue(value);
    }
}
