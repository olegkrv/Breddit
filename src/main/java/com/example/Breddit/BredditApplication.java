package com.example.Breddit;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.Breddit.models.Code;
import com.example.Breddit.models.CurrentUser;


@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.example.Breddit.repository")
//@EntityScan(basePackages = "com.example.Breddit.repository")
public class BredditApplication {
	public static void main(String[] args) {
		SpringApplication.run(BredditApplication.class, args);
	}
}

