package com.example.hh99miniproject8.controller;

import com.example.hh99miniproject8.dto.comment.CommentRequestDto;
import com.example.hh99miniproject8.dto.comment.CommentResponseDto;
import com.example.hh99miniproject8.dto.post.PostResponseDto;
import com.example.hh99miniproject8.security.jwt.UserDetailsImpl;
import com.example.hh99miniproject8.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin
public class CommentController {

    private final CommentService commentService;

    //댓글 생성 API
    @PostMapping("/comment/{id}")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("reqeust in CommentController");
        return commentService.createComment(id, requestDTO, userDetails.getUser());
    }
    //댓글 수정 API
    @PutMapping("/comment/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(id, requestDTO, userDetails.getUser());
    }

    //댓글 삭제 API
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(id, userDetails.getUser());
    }



}
