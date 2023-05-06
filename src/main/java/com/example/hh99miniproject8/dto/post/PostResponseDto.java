package com.example.hh99miniproject8.dto.post;

import com.example.hh99miniproject8.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private int goodCount;
    //private List<CommentResponseDTO> comments;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.nickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.category = post.getCategory();
        this.goodCount = post.getGoodCount();
        //this.comments = post.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }


}
