package com.example.hh99miniproject8.entity;

import jakarta.persistence.*;
import com.example.hh99miniproject8.dto.comment.CommentRequestDto;
import com.example.hh99miniproject8.dto.post.PostRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String address;

    public Comment(CommentRequestDto request) {
        this.content = request.getContent();
    }

    public void update(CommentRequestDto request) {
        this.content = request.getContent();
    }

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}
