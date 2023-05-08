package com.example.hh99miniproject8.controller;

import com.example.hh99miniproject8.dto.comment.CommentRequestDto;
import com.example.hh99miniproject8.dto.comment.CommentResponseDto;
import com.example.hh99miniproject8.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 생성 API
    @PostMapping("/comment/{id}")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDTO, HttpServletRequest httpServletRequest) {
        return commentService.createComment(id, requestDTO, httpServletRequest);
    }
    //댓글 수정 API
    @PutMapping("/comment/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDTO, HttpServletRequest httpServletRequest) {
        return commentService.updateComment(id, requestDTO, httpServletRequest);
    }

    //댓글 삭제 API
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        return commentService.deleteComment(id, httpServletRequest);
    }



}
