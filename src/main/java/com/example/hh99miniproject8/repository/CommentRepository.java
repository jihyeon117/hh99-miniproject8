package com.example.hh99miniproject8.repository;

import com.example.hh99miniproject8.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
