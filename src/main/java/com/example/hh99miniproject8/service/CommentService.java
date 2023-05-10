package com.example.hh99miniproject8.service;

import com.example.hh99miniproject8.dto.comment.CommentRequestDto;
import com.example.hh99miniproject8.dto.comment.CommentResponseDto;
import com.example.hh99miniproject8.entity.Comment;
import com.example.hh99miniproject8.entity.Post;
import com.example.hh99miniproject8.entity.User;
import com.example.hh99miniproject8.exception.CustomException;
import com.example.hh99miniproject8.repository.CommentRepository;
import com.example.hh99miniproject8.repository.PostRepository;
import com.example.hh99miniproject8.repository.UserRepository;
import com.example.hh99miniproject8.security.jwt.JwtProvider;
import com.example.hh99miniproject8.security.jwt.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hh99miniproject8.exception.ErrorCode.*;
import static com.example.hh99miniproject8.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    //========================API 메서드==========================
    //댓글 생성 API
    @Transactional
    public ResponseEntity<CommentResponseDto> createComment(Long id, CommentRequestDto requestDTO, User user) {
        Post post = isPostExist(id);
        Comment comment = commentRepository.save(new Comment(requestDTO, user, post));
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentResponseDto(comment));
    }

    //댓글 수정 API
    @Transactional
    public ResponseEntity<CommentResponseDto> updateComment(Long id, CommentRequestDto requestDTO, User user) {
        // 게시글 존재유무 체크
        Comment comment = isCommentExist(id);
        // 작성자와, 유저 매치체크
        if(!checkAuthorIdMatch(comment, user)) {
            throw new CustomException(WRITER_ONLY_MODIFY);
        }
        comment.update(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new CommentResponseDto(comment));
    }

    //댓글 삭제 API
    @Transactional
    public ResponseEntity<String> deleteComment(Long id, User user) {
        // 게시글 존재유무 체크
        Comment comment = isCommentExist(id);
        // 작성자와, 유저 매치체크
        if(!checkAuthorIdMatch(comment, user))
            throw new CustomException(WRITER_ONLY_DELETE);

        commentRepository.delete(comment);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 식제 성공");
    }
    //========================API 메서드==========================


    //========================외부 메서드==========================
    // 댓글 존재 여부 확인
    // chore 석빈
    // check는 여러가지 의미를 가진 단어이고 메서드에서 수행하는 동작을 직관적으로 알기 힘들기에
    // checkComment -> isCommentExist 로 변경했습니다.
    private Comment isCommentExist(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(COMMENT_NOT_FOUND)
        );
        return comment;
    }

    // 게시글 존재 여부 확인
    private Post isPostExist(Long id){
        return postRepository.findById(id).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );
    }

    // 게시글과 게시글을 변경하려는 요청을 보낸 유저가 일치하는지 여부 체크 default return value : true
    private boolean checkAuthorIdMatch(Comment comment, User user){
        if(comment.getUser().getUsername().equals(user.getUsername()))
            return false;
        return true;
    }
    //========================외부 메서드==========================
}
