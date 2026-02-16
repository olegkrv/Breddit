package com.example.Breddit.repository.Interfaces;

import java.util.List;

import com.example.Breddit.models.Sub;

public interface SubPreRepository {

    List<Sub> findAll();

    Sub save(Sub sub);

    Sub findByid(Long id);

    Sub findBytitle(String title);

    void deleteByid(Long id);

}
