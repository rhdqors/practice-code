package com.sparta.myselectshop.naver.controller;


import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.naver.service.NaverApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // json형식으로 객체 데이터를 반환 ?
@RequestMapping("/api") // 여기의 모든 요청들은 /api로 받겠다
@RequiredArgsConstructor
// final이나 @NonNull인 필드 값만 파라미터로 받는 생성자 만듦
// 생성자 주입을 임의의 코드없이 자동으로 설정
public class NaverApiController {

    private final NaverApiService naverApiService;

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String query)  {
        return naverApiService.searchItems(query);
    }
}