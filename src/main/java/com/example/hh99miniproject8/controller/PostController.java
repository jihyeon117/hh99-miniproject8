package com.example.hh99miniproject8.controller;

import com.example.hh99miniproject8.dto.post.PostRequestDto;
import com.example.hh99miniproject8.dto.post.PostResponseDto;
import com.example.hh99miniproject8.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PostController {

    private final PostService postservice;


    //게시글 생성 API
    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDTO, HttpServletRequest httpServletRequest) {
        return postservice.createPost(requestDTO, httpServletRequest);
    }

    //게시글 전체 조회 API
    @GetMapping("/post")
    public ResponseEntity<Map<String, List<PostResponseDto>>> listPosts() {
        return postservice.listPosts();
    }

    //게시글 단일 조회 API
    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> singlePost(@PathVariable Long id) {
        return postservice.singlePost(id);
    }

    //게시글 수정 API
    @PutMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDTO, HttpServletRequest httpServletRequest) {
        return postservice.updatePost(id, requestDTO, httpServletRequest);
    }

    //게시글 삭제 API
    @DeleteMapping("/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        return postservice.deletePost(id, httpServletRequest);
    }



}
