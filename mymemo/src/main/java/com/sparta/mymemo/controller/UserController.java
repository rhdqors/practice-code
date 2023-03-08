package com.sparta.mymemo.controller;

import com.sparta.mymemo.dto.LoginRequestDto;
import com.sparta.mymemo.dto.ResponseCodeDto;
import com.sparta.mymemo.dto.SignupRequestDto;
import com.sparta.mymemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseCodeDto> signup(@Valid  @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return new ResponseEntity<>(new ResponseCodeDto("회원가입 성공", HttpStatus.OK.value()), HttpStatus.OK);
    }

    // 로그인
    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<ResponseCodeDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return new ResponseEntity<>(new ResponseCodeDto("로그인 성공", HttpStatus.OK.value()), HttpStatus.OK);

    }
}
