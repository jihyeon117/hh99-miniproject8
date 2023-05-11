package com.example.hh99miniproject8.controller;

import com.example.hh99miniproject8.dto.post.PostRequestDto;
import com.example.hh99miniproject8.dto.post.PostResponseDto;
import com.example.hh99miniproject8.security.jwt.UserDetailsImpl;
import com.example.hh99miniproject8.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin
public class PostController {

    private final PostService postService;


    //게시글 생성 API
    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponseDto> createPost(@RequestPart("requestDTO") PostRequestDto requestDTO,
                                                      @RequestPart("image") MultipartFile image,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        PostResponseDto responseDto = postService.createPost(requestDTO, image, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }

    //게시글 전체 조회 API
    @GetMapping("/posts")
    public ResponseEntity<Map<String, List<PostResponseDto>>> listPosts() {
        return postService.listPosts();
    }

    //게시글 단일 조회 API
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> singlePost(@PathVariable Long id) {
        return postService.singlePost(id);
    }

    //게시글 수정 API
    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, requestDTO, userDetails.getUser());
    }

    //게시글 삭제 API
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getUser());
    }
}