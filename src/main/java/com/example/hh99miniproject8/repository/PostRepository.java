package com.example.hh99miniproject8.repository;

import com.example.hh99miniproject8.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

}
