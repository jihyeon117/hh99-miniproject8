package com.example.hh99miniproject8.entity;

import com.example.hh99miniproject8.dto.post.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor
@Getter
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @ColumnDefault("0")
    private int goodCount;
    private String category;

    public Post(PostRequestDto request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.category = request.getCategory();
    }

    public void update(PostRequestDto request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }
}
