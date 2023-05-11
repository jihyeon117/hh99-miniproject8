package com.example.hh99miniproject8.service;

import com.example.hh99miniproject8.dto.post.PostRequestDto;
import com.example.hh99miniproject8.dto.post.PostResponseDto;
import com.example.hh99miniproject8.entity.Post;
import com.example.hh99miniproject8.entity.RoleTypeEnum;
import com.example.hh99miniproject8.entity.User;
import com.example.hh99miniproject8.exception.CustomException;
import com.example.hh99miniproject8.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.hh99miniproject8.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    //========================API 메서드==========================
    //게시글 생성 API
    @Transactional
    public ResponseEntity<PostResponseDto> createPost(PostRequestDto requestDTO, User user) {
        Post post = postRepository.saveAndFlush(new Post(requestDTO, user));
        return ResponseEntity.status(HttpStatus.CREATED).body(new PostResponseDto(post));
    }

    //게시글 전체 조회 API
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, List<PostResponseDto>>> listPosts() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postList = new ArrayList<>();
        for (Post post : posts) {
            postList.add(new PostResponseDto(post));
        }

        Map<String, List<PostResponseDto>> result = new HashMap<>();
        result.put("postList", postList);
        return ResponseEntity.ok().body(result);
    }

    //게시글 단일 조회 API
    @Transactional(readOnly = true)
    public ResponseEntity<PostResponseDto> singlePost(Long id) {
        // 게시글 존재 유무체크
        Post post = isPostExist(id);
        return ResponseEntity.status(HttpStatus.OK).body(new PostResponseDto(post));
    }

    //게시글 수정 API
    @Transactional
    public ResponseEntity<PostResponseDto> updatePost(Long id, PostRequestDto requestDTO, User user) {
        // 게시글 존재 유무체크
        Post post = isPostExist(id);
        // 게시글 작성자와, 유저 매치 체크
        if(checkAuthorIdMatch(post, user)){
            throw new CustomException(WRITER_ONLY_MODIFY);
        }
        post.update(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new PostResponseDto(post));
    }

    //게시글 삭제 API
    @Transactional
    public ResponseEntity<String> deletePost(Long id, User user) {
        // 게시글 존재 유무체크
        Post post = isPostExist(id);
        // 게시글 작성자와, 유저 매치 체크
        if(checkAuthorIdMatch(post, user)) {
            throw new CustomException(WRITER_ONLY_DELETE);
        }
        postRepository.delete(post);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 식제 성공");

    }
    //========================API 메서드==========================

    //========================외부 메서드==========================
    // id를 매개변수로 받아서 id에 대응되는 게시글이 존재하는지 체크하는 메서드
    private Post isPostExist(Long id){
        return postRepository.findById(id).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );
    }

    // 게시글과 게시글을 변경하려는 요청을 보낸 유저가 일치하는지 여부 체크 default return value : true
    private boolean checkAuthorIdMatch(Post post, User user){
        if(post.getUser().getUsername().equals(user.getUsername()) || user.getRole() == RoleTypeEnum.ADMIN)
            return false;
        return true;
    }
    //========================외부 메서드==========================
}
