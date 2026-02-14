package com.example.Breddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.example.Breddit.models.Code;

@EnableJpaRepositories
@Repository
public interface CodeRepository extends JpaRepository<Code, String> {
    public Code findByvalue(String value);
}
