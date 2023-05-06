package com.example.hh99miniproject8.dto.comment;

import com.example.hh99miniproject8.entity.Comment;
import com.example.hh99miniproject8.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String address;


    public CommentResponseDto(Comment comment) {
        this.content = comment.getContent();
        this.address = comment.getAddress();
        this.createdAt = comment.getCreatedAt();

    }

}
