package com.sparta.mymemo.controller;

import com.sparta.mymemo.dto.LoginRequestDto;
import com.sparta.mymemo.dto.ResponseCodeDto;
import com.sparta.mymemo.dto.SignupRequestDto;
import com.sparta.mymemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    // 회원가입 페이지
    @GetMapping("/signup")
    public ModelAndView signupPage() {
        return null;
//        return new ModelAndView("signup");
    }
    // 로그인 페이지
    @GetMapping("/login")
    public ModelAndView loginPage() {
        return null;
//        return new ModelAndView("login");
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseCodeDto> signup(@Valid  @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return new ResponseEntity<>(new ResponseCodeDto("회원가입 성공", 200), HttpStatus.OK);
//        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공.");
//        ResponseEntity.status(HttpStatus.CREATED);
//        return new ResponseEntity(new ResponseCodeDto("회원가입 성공"), HttpStatus.OK);
//        return "redirect:/api/user/login"; 회원가입 성공 > 로그인페이지 이동
//        @RequestBody redirect에는 필요없나?
    }

    // 로그인
    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<ResponseCodeDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return new ResponseEntity<>(new ResponseCodeDto("로그인 성공", 200), HttpStatus.OK);
//        return new ResponseEntity(new ResponseCodeDto("로그인 성공"), HttpStatus.OK);
    }
}
