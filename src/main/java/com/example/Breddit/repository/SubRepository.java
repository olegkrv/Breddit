package com.example.Breddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.example.Breddit.models.Sub;

@EnableJpaRepositories
@Repository
public interface SubRepository extends JpaRepository<Sub, Long> {
    public void deleteByid(Long id);

    public Sub findByid(Long id);

    public Sub findBytitle(String title);
}
