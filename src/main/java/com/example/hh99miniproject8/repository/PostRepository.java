package com.example.hh99miniproject8.repository;

import com.example.hh99miniproject8.entity.Post;
import com.example.hh99miniproject8.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc();

    Optional<Post> findByIdAndUserId(Long id, User user);
}
