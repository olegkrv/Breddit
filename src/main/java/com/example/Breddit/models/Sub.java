package com.example.Breddit.models;

import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "SubBreddits")
public class Sub extends OperationsWithPost{
    public Sub() {super();}

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String title;
    private String description;
    private ArrayList<Long> posts = new ArrayList<Long>();
    private Long main_admin; 
    private ArrayList<Long> admins = new ArrayList<Long>();
}
