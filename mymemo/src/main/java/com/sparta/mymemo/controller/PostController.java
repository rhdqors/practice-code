package com.sparta.mymemo.controller;

import com.sparta.mymemo.dto.PostRequestDto;
import com.sparta.mymemo.dto.PostResponseDto;
import com.sparta.mymemo.dto.ResponseCodeDto;
import com.sparta.mymemo.entity.Post;
import com.sparta.mymemo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController // json 형태로 데이터 반환?
@RequiredArgsConstructor // 생성자 부분의 autowired 필요없음
//@RequestMapping("/api")
public class PostController {

    private final PostService postService;
//    private final TimeStamped timeStamped;

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    // 글 생성
    @PostMapping("/api/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) { // dto를 문자열로 받아오겠다 ?
        return postService.createPost(requestDto, request);
    }

    // 전체 글 조회
    @GetMapping("/products")
    public Page<Post> getProducts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            HttpServletRequest request
    ) {
        // 응답 보내기
        return postService.getPosts(request, page-1, size, sortBy, isAsc);
    }

    // 선택 글 조회
    @GetMapping("/api/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id/*url에서 입력한id*/, HttpServletRequest request) {
        return postService.getPost(id/*url에서 입력한id*/, request); // service의 getmemo메서드에 id값을 가져감
        // 마지막 단계 - service에서 리턴한 memo가 바로 memoService.getMemo(id)로 되어 출력
    }

    // 글 수정
    @PutMapping("/api/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {// body에 dto를 json형식으로 받음
        return postService.updatePost(id, requestDto, request);
    }

    // 글 삭제
    @DeleteMapping("/api/post/{id}")
    public ResponseEntity<ResponseCodeDto> deleteMemo(@PathVariable Long id, HttpServletRequest request) {
        postService.deletePost(id, request);
        return new ResponseEntity<>(new ResponseCodeDto("게시글 삭제", 200), HttpStatus.OK);
    }



}
