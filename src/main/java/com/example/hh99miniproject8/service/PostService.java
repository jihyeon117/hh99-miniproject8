package com.example.hh99miniproject8.service;

import com.example.hh99miniproject8.dto.post.PostRequestDto;
import com.example.hh99miniproject8.dto.post.PostResponseDto;
import com.example.hh99miniproject8.entity.Post;
import com.example.hh99miniproject8.exception.CustomException;
import com.example.hh99miniproject8.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.hh99miniproject8.exception.ErrorCode.POST_NOT_FOUND;



@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    //게시글 생성 API
    @Transactional
    public ResponseEntity<PostResponseDto> createPost(PostRequestDto requestDTO) {
        Post post = postRepository.save(new Post(requestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(new PostResponseDto(post));
    }

    //게시글 전체 조회 API
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, List<PostResponseDto>>> listPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponseDto> postList = new ArrayList<>();
        for(Post post : posts) {
            postList.add(new PostResponseDto(post));
        }

        Map<String, List<PostResponseDto>> result = new HashMap<>();
        result.put("postList", postList);
        return ResponseEntity.ok().body(result);
    }

    //게시글 단일 조회 API
    @Transactional(readOnly = true)
    public ResponseEntity<PostResponseDto> singlePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );
        return ResponseEntity.status(HttpStatus.OK).body(new PostResponseDto(post));
    }

    //게시글 수정 API
    @Transactional
    public ResponseEntity<PostResponseDto> updatePost(Long id, PostRequestDto requestDTO) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );
        post.update(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new PostResponseDto(post));

    }

    //게시글 삭제 API
    @Transactional
    public ResponseEntity<String> deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );

        postRepository.delete(post);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 식제 성공");

    }
}
