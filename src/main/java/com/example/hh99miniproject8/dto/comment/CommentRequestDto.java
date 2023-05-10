package com.example.hh99miniproject8.dto.comment;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    // Chore 석빈
    // post_id는 PathValue로 받을 수 있기에 주석처리 하겠습니다.
//    private Long post_id;
    private String content;
}
