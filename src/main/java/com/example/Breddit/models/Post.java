package com.example.Breddit.models;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NonNull;



@Data
@Entity
@Table(name = "Breddit Posts")
public class Post {
    public Post() {super();}

    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String title;
    private String description;
    private Long author;
    private Long sub_id;
    private Instant date;
}


