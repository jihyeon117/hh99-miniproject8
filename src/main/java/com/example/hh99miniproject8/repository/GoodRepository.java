package com.example.hh99miniproject8.repository;

import com.example.hh99miniproject8.entity.Good;
import com.example.hh99miniproject8.entity.Post;
import com.example.hh99miniproject8.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodRepository extends JpaRepository<Good, Long> {
    Good findByPostAndUser(Post post, User user);
}
