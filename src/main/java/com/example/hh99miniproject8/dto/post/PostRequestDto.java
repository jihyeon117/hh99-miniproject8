package com.example.hh99miniproject8.dto.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostRequestDto {

    private String title;       // 게시글 제목
    private String content;     // 게시글 내용
    private String category;    // 게시글 카테고리
    private String region;      // 게시자 주소자
    private MultipartFile image;
}