package com.sparta.mymemo.service;

import com.sparta.mymemo.dto.PostRequestDto;
import com.sparta.mymemo.dto.PostResponseDto;
import com.sparta.mymemo.entity.Post;
import com.sparta.mymemo.entity.User;
import com.sparta.mymemo.jwt.JwtUtil;
import com.sparta.mymemo.repository.PostRepository;
import com.sparta.mymemo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor // 추적
public class PostService {

    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private User tokenMatchUser(HttpServletRequest request) {
        // 토큰 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 가능
        User user;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("토큰이 없습니다.");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            user = userRepository.findByUsername(claims.getSubject()).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        } else {
            throw new IllegalArgumentException("Token missing");
        }
        return user;
    }

    // 글 생성
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
        User user = tokenMatchUser(request);

        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Post post = postRepository.saveAndFlush(new Post(requestDto, user));

        return new PostResponseDto(post);

    }

    // 전체 글 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts() {
        List<Post> postlist = postRepository.findAllByOrderByCreatedAtDesc();

        // 형변환
        List<PostResponseDto> responselist = new ArrayList<>();

        for (Post post : postlist) {
            responselist.add(new PostResponseDto(post));
        }

        return responselist;

    }

    // 선택 글 조회 - user 확인하는 토큰을 가져올 필요 없음
    @Transactional
    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다.")); // 게시글 유무 확인
        return new PostResponseDto(post); // 게시글 존재 > 형식 맞춰서 return
    }

    // 글 수정
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, HttpServletRequest request) {
        User user = tokenMatchUser(request);
        Post post = postMatchUser(postId, user);
//        Post post = postRepository.findByIdAndUsername(postId, user.getUsername()).orElseThrow(() -> new IllegalArgumentException("유저와 게시글이 일치하지 않습니다.")); // 로그인 유저가 작성한 글인지 확인

        post.updateMemo(requestDto);
        return new PostResponseDto(post);
    }

    // 글 삭제
    @Transactional
    public void deletePost(Long postId, HttpServletRequest request) {
        User user = tokenMatchUser(request);
        Post post = postMatchUser(postId, user);
//        Post post = postRepository.findByIdAndUsername(postId, user.getUsername()).orElseThrow(() -> new IllegalArgumentException("유저와 게시글이 일치하지 않습니다.")); // 로그인 유저가 작성한 글인지 확인
        postRepository.deleteById(post.getId());

    }

    private Post postMatchUser(Long postId, User user) {
        Post post = postRepository.findByIdAndUsername(postId, user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("유저와 게시글이 일치하지 않습니다.")); // 로그인 유저가 작성한 글인지 확인
        return post;
    }
}


