package com.example.hh99miniproject8.controller;

import com.example.hh99miniproject8.dto.post.PostRequestDto;
import com.example.hh99miniproject8.dto.post.PostResponseDto;
import com.example.hh99miniproject8.security.jwt.UserDetailsImpl;
import com.example.hh99miniproject8.service.PostService;
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
public class PostController {

    private final PostService postservice;


    //게시글 생성 API
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postservice.createPost(requestDTO, userDetails.getUser());
    }

    //게시글 전체 조회 API
    @GetMapping("/posts")
    public ResponseEntity<Map<String, List<PostResponseDto>>> listPosts() {
        return postservice.listPosts();
    }

    //게시글 단일 조회 API
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> singlePost(@PathVariable Long id) {
        return postservice.singlePost(id);
    }

    //게시글 수정 API
    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postservice.updatePost(id, requestDTO, userDetails.getUser());
    }

    //게시글 삭제 API
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postservice.deletePost(id, userDetails.getUser());
    }



}
