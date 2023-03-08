package com.sparta.mymemo.service;

import com.sparta.mymemo.dto.PostRequestDto;
import com.sparta.mymemo.dto.PostResponseDto;
import com.sparta.mymemo.dto.SignupRequestDto;
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

    // 글 생성
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 관심상품 추가 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("토큰이 없습니다.");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            Post post = postRepository.saveAndFlush(new Post(requestDto, user));

            return new PostResponseDto(post);
        } else {
            return null;
        }

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
    public PostResponseDto getPost(Long id) {
        Post post = checkPost(id); // 게시글 유무 확인
        return new PostResponseDto(post); // 게시글 존재 > 형식 맞춰서 return
    }

    // 글 수정
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        Post post = checkPost(id); // 게시글 있는지 유효성 검사
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("토큰이 없습니다.");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

//            // 요청받은 DTO 로 DB에 저장할 객체 만들기
//            Post post = postRepository.saveAndFlush(new Post(requestDto, user.getId()));
            post.updateMemo(requestDto); // 오류 없으면 수정해야할 메모 있다 판단. - 가지고온 데이터로 변경, memo entity에서 update메서드를 만들어 처리할거임
            return new PostResponseDto(post);
        } else {
            return null;
        }
//        checkPw(memo, requestDto); //아래 유효성검사 메소드
//        return new PostResponseDto(post);
    }

    @Transactional // 글 삭제
    public void deletePost(Long id, SignupRequestDto signupRequestDto, HttpServletRequest request) {
        checkPost(id); // 게시글 있는지 유효성 검사
//        checkPw(memo, requestDto); //아래 유효성검사 메소드
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        String username = signupRequestDto.getUsername();

        // 토큰이 있는 경우에만 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("토큰이 없습니다.");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            ); // 로그인한 유저만 가져옴

            // db에 로그인유저가 있는지 확인
            if (user.getUsername().equals(username)) {
                postRepository.deleteById(id);
            } else {
                throw new IllegalArgumentException("유저가 일치하지 않습니다.");
            }
//            // 요청받은 DTO 로 DB에 저장할 객체 만들기
//            Post post = postRepository.saveAndFlush(new Post(requestDto, user.getId()));
        }

    }

    private Post checkPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
    }

//    private void checkPw(Memo memo, MemoRequestDto requestDto) {
//        if (!StringUtils.equals(memo.getPassword(), requestDto.getPassword())) { //
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//        // Objects.equals - null값이면 오류난다
//        // StringUtils(Thymeleaf) - 다 된다
//        // 아래 방식은 null이나 다른값이 올때 오류?.
////        if (!memo.getPassword().equals(requestDto.getPassword())) {
////            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
////        }
//    }

}
