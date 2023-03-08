package com.sparta.mymemo.service;

import com.sparta.mymemo.dto.LoginRequestDto;
import com.sparta.mymemo.dto.SignupRequestDto;
import com.sparta.mymemo.entity.User;
import com.sparta.mymemo.entity.UserRoleEnum;
import com.sparta.mymemo.jwt.JwtUtil;
import com.sparta.mymemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 회원가입
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // 회원 중복, 조건 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            // Optional에서 found값을 찾아서 있으면 true
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        UserRoleEnum role = UserRoleEnum.USER;
//        if (username.matches("^[0-9a-z]{4,10}$") && password.matches("^[0-9a-zA-Z]{8,15}$")) {
            User user = new User(username, password, role);
            userRepository.save(user);
//            ResponseEntity<String> response = ResponseEntity.ok(successMessage);
//            return response;
//        }
//        else {
//            throw new IllegalArgumentException("이름과 비밀번호를 확인하세요.");
//        }

    }

    // 로그인
    @Transactional
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // db에 저장된 이름, 암호 확인
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));
        if (!password.equals(user.getPassword()) ) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));


    }
}
