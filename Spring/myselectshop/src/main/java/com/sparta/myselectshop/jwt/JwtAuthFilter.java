package com.sparta.myselectshop.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myselectshop.dto.SecurityExceptionDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request);

        if(token != null) {
            if(!jwtUtil.validateToken(token)){
                jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
                return;
            } // 여기서 토큰에대한 오류 발생시 아래의 jwtExceptionHandler 메소드를 통해 커스터마이징 해서 예외처리값을 알려준다.
            // 토큰에 이상이 없다면 아래 로직 실행.
            Claims info = jwtUtil.getUserInfoFromToken(token); // 토큰에서 user의 정보를 Claims에 가져옴
            setAuthentication(info.getSubject()); // setAuthentication 함수에 info에 넣어둔 username을 보낸다
        }
        filterChain.doFilter(request,response);
    }

    public void setAuthentication(String username) { // 위에서 보낸 username을을 받음
        SecurityContext context = SecurityContextHolder.createEmptyContext(); // securitycontext를 만들어서
        Authentication authentication = jwtUtil.createAuthentication(username); // 그 안에 authentication 인증 객체를 넣고
        context.setAuthentication(authentication); // securitycontext홀더에 넣는다

        SecurityContextHolder.setContext(context); // securitycontext홀더에 넣고 인증.
        // 그리고 다음 filter로 넘어갈 시 security에서는 이것이 인증이 되었다 인지하고 controller까지 요청이 넘어감
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}