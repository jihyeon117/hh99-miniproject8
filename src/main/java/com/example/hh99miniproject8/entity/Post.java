package com.example.hh99miniproject8.entity;

import com.example.hh99miniproject8.dto.comment.CommentResponseDto;
import com.example.hh99miniproject8.dto.post.PostRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String category;

    @NotNull
    private String region;

    @Column(length = 500, nullable = false)
    private String imageUrl;

    @ColumnDefault("0")
    private int goodCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @OrderBy("createdAt desc")
    private List<Comment> comments;

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
        this.imageUrl = postRequestDto.getImageUrl();
        this.user = user;
    }

    public void update(PostRequestDto postRequestDto, User user) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.user = user;
    }

    public void togglLike(boolean likeIoN) {
        if(likeIoN == true) {
            this.goodCount = this.goodCount + 1;
        } else if(likeIoN == false) {
            this.goodCount = this.goodCount - 1;
        }
    }
}
