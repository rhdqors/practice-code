package com.sparta.mymemo.controller;

import com.sparta.mymemo.dto.PostRequestDto;
import com.sparta.mymemo.dto.PostResponseDto;
import com.sparta.mymemo.dto.ResponseCodeDto;
import com.sparta.mymemo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor

public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    // 글 생성
    @PostMapping("/api/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.createPost(requestDto, request);
    }

    // 전체 글 조회
    @GetMapping("/api/posts")
    public List<PostResponseDto> getPosts() {
        return postService.getPosts();
    }

    // 선택 글 조회
    @GetMapping("/api/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 글 수정
    @PutMapping("/api/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.updatePost(id, requestDto, request);
    }

    // 글 삭제
    @DeleteMapping("/api/post/{id}")
    public ResponseEntity<ResponseCodeDto> deleteMemo(@PathVariable Long id, HttpServletRequest request) {
        postService.deletePost(id, request);
        return new ResponseEntity<>(new ResponseCodeDto("게시글 삭제", HttpStatus.OK.value()), HttpStatus.OK);
    }



}
