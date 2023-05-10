package com.example.hh99miniproject8.entity;

import com.example.hh99miniproject8.dto.comment.CommentResponseDto;
import com.example.hh99miniproject8.dto.post.PostRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String category;

    @NotNull
    private String region;


    @ColumnDefault("0")
    private int goodCount;


    // 댓글
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @OrderBy("createdAt desc")
    private List<Comment> comments = new ArrayList<>();

    // 좋아요
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Good> goods = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Post(PostRequestDto postRequestDto, User user) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.category = postRequestDto.getCategory();
        this.region = postRequestDto.getRegion();
        this.user = user;
    }

    // chore 석빈
    // post를 업데이트해도 작성자는 바뀌지 않으니 user를 업데이트 해서는 안됨
    // 특히 admin이 수정을 할경우 작성자가 admin이 되버림
    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        // 카테고리 수정안함
//        this.category = postRequestDto.getCategory();
//        this.user = user;
    }

    public void togglLike(boolean likeIoN) {
        if(likeIoN == true) {
            this.goodCount = this.goodCount + 1;
        } else if(likeIoN == false) {
            this.goodCount = this.goodCount - 1;
        }
    }
}
