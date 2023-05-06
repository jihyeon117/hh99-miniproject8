package com.example.hh99miniproject8.service;

import com.example.hh99miniproject8.dto.comment.CommentRequestDto;
import com.example.hh99miniproject8.dto.comment.CommentResponseDto;
import com.example.hh99miniproject8.dto.post.PostRequestDto;
import com.example.hh99miniproject8.dto.post.PostResponseDto;
import com.example.hh99miniproject8.entity.Comment;
import com.example.hh99miniproject8.entity.Post;
import com.example.hh99miniproject8.entity.User;
import com.example.hh99miniproject8.exception.CustomException;
import com.example.hh99miniproject8.repository.CommentRepository;
import com.example.hh99miniproject8.repository.PostRepository;
import com.example.hh99miniproject8.repository.UserRepository;
import com.example.hh99miniproject8.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hh99miniproject8.exception.ErrorCode.*;
import static com.example.hh99miniproject8.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //댓글 생성 API
    @Transactional
    public ResponseEntity<CommentResponseDto> createComment(Long id, CommentRequestDto requestDTO, HttpServletRequest httpServletRequest) {
        User user = tokenCheck(httpServletRequest);
        Comment comment = commentRepository.save(new Comment(requestDTO, user));
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentResponseDto(comment));

    }

    //댓글 수정 API
    @Transactional
    public ResponseEntity<CommentResponseDto> updateComment(Long id, CommentRequestDto requestDTO, HttpServletRequest httpServletRequest) {
        User user = tokenCheck(httpServletRequest);
        Comment comment = checkComment(id);
        comment.update(requestDTO, user);
        return ResponseEntity.status(HttpStatus.OK).body(new CommentResponseDto(comment));

    }

    //댓글 삭제 API
    @Transactional
    public ResponseEntity<String> deleteComment(Long id, HttpServletRequest httpServletRequest) {
        User user = tokenCheck(httpServletRequest);
        Comment comment = checkComment(id);
        commentRepository.delete(comment);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 식제 성공");

    }


    // 토큰 검사
    public User tokenCheck(HttpServletRequest httpServletRequest) {
        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if (token != null) {
            if (jwtUtil.vaildateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new CustomException(TOKEN_NOT_FOUND);
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new CustomException(USER_NOT_FOUND)
            );
            return user;

        }
        return null;
    }

    // 댓글 존재 여부 확인
    private Comment checkComment(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(COMMENT_NOT_FOUND)
        );
        return comment;
    }

    // 게시글 존재 여부 확인
    private Post checkPost(Long id){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );
        return post;
    }
}


