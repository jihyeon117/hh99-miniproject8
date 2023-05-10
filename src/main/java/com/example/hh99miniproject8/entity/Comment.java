package com.example.hh99miniproject8.entity;

import com.example.hh99miniproject8.dto.comment.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private String content;
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(CommentRequestDto commentRequestDto, User user, Post post) {
        this.content = commentRequestDto.getContent();
        this.address = user.getAddress();
        this.user = user;
        this.post = post;
    }

    // Chore 석빈
    // update는 본인만 할 수 있기때문에 user는 변경이 없음으로 매개변수와 this.user = user 제거
    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
        this.address = user.getAddress();
    }
}
