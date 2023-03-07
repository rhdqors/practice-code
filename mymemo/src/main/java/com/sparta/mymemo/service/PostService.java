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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor // 추적
public class PostService {

    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Transactional // 글 생성
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
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            Post post = postRepository.saveAndFlush(new Post(requestDto, user.getId()));

            return new PostResponseDto(post);
        } else {
            return null;
        }

    }

    @Transactional(readOnly = true)
    public Page<Post> getPosts(HttpServletRequest request, int page, int size, String sortBy, boolean isAsc) {
        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 전체 글 조회 가능
        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 전체 글 조회
            return postRepository.findAll(pageable);
        } else {
            return null;
        }
    }


//    @Transactional(readOnly = true) // 전체 글 조회
//    public List<PostResponseDto> getPosts(HttpServletRequest request) {
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        // 토큰이 있는 경우에만 관심상품 조회 가능
//        if (token != null) {
//            // Token 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
//            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
//            );
//
////            UserRoleEnum userRoleEnum = user.getRole();
////            System.out.println("role = " + userRoleEnum);
//
////            List<Post> postList;
//
////            if (userRoleEnum == UserRoleEnum.USER) {
////                // 사용자 권한이 USER일 경우
////                List<Post> postList = postRepository.findAllByUserId(user.getId());
////            }
//
//        }
//        return postRepository.findAllByOrderByCreatedAtDesc();
//
//    }

    @Transactional // 선택 글 조회
    public PostResponseDto getPost(Long id/*controller에서 가져온 id값*/, HttpServletRequest request) {
        Post post = checkId(id); //아래 유효성검사 메소드
        return new PostResponseDto(post); // 아이디 일치하면 memo 리턴
    }

    @Transactional // 글 수정
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        Post post = checkId(id); //아래 유효성검사 메소드
//        checkPw(memo, requestDto); //아래 유효성검사 메소드
        post.updateMemo(requestDto); // 오류 없으면 수정해야할 메모 있다 판단. - 가지고온 데이터로 변경, memo entity에서 update메서드를 만들어 처리할거임
        return new PostResponseDto(post);
    }

    @Transactional // 글 삭제
    public void deletePost(Long id, HttpServletRequest request) {
        checkId(id); //아래 유효성검사 메소드
//        checkPw(memo, requestDto); //아래 유효성검사 메소드
        postRepository.deleteById(id);
    }

    private Post checkId(Long id) {
        return postRepository.findById(id/*controller에서 가져온 id값*/).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")// 오류 발생시
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
