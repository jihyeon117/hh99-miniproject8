package com.example.hh99miniproject8.dto.post;

import com.example.hh99miniproject8.dto.comment.CommentResponseDto;
import com.example.hh99miniproject8.entity.Comment;
import com.example.hh99miniproject8.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String category;
    private String region;
    private List<CommentResponseDto> comments;
    private int goodCount;
    private String imageUrl;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.nickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.category = post.getCategory();
        this.region = post.getRegion();
        if (post.getComments() == null) {
            this.comments = Collections.emptyList();
        } else {
            this.comments = post.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        }
        this.goodCount = post.getGoodCount();
        this.imageUrl = post.getImageUrl();
    }

}
