package com.example.hh99miniproject8.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //400, BAD_REQUEST
    USER_Duplicate(HttpStatus.BAD_REQUEST,"중복된 username 입니다."),
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST,"토큰이 유효하지 않습니다."),
    WRITER_ONLY_MODIFY(HttpStatus.BAD_REQUEST,"작성자만 수정할 수 있습니다."),
    WRITER_ONLY_DELETE(HttpStatus.BAD_REQUEST,"작성자만 삭제할 수 있습니다."),

    //404, NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"회원을 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,"게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"댓글을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;

}
