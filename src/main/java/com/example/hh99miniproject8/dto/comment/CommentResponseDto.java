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
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String address;


    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.address = comment.getUser().getAddress();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();

    }

}
