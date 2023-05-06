package com.example.hh99miniproject8.service;

import com.example.hh99miniproject8.dto.comment.CommentRequestDto;
import com.example.hh99miniproject8.dto.comment.CommentResponseDto;
import com.example.hh99miniproject8.dto.post.PostRequestDto;
import com.example.hh99miniproject8.dto.post.PostResponseDto;
import com.example.hh99miniproject8.entity.Comment;
import com.example.hh99miniproject8.entity.Post;
import com.example.hh99miniproject8.exception.CustomException;
import com.example.hh99miniproject8.repository.CommentRepository;
import com.example.hh99miniproject8.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hh99miniproject8.exception.ErrorCode.COMMENT_NOT_FOUND;
import static com.example.hh99miniproject8.exception.ErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    //댓글 생성 API
    @Transactional
    public ResponseEntity<CommentResponseDto> createComment(CommentRequestDto requestDTO) {
        Comment comment = commentRepository.save(new Comment(requestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentResponseDto(comment));

    }

    //댓글 수정 API
    @Transactional
    public ResponseEntity<CommentResponseDto> updateComment(Long id, CommentRequestDto requestDTO) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(COMMENT_NOT_FOUND)
        );
        comment.update(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new CommentResponseDto(comment));

    }

    //댓글 삭제 API
    @Transactional
    public ResponseEntity<String> deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(COMMENT_NOT_FOUND)
        );

        commentRepository.delete(comment);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 식제 성공");

    }
}


