package com.sparta.mymemo.controller;

import com.sparta.mymemo.dto.MemoRequestDto;
import com.sparta.mymemo.dto.MemoResponseDto;
import com.sparta.mymemo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController // json 형태로 데이터 반환?
@RequiredArgsConstructor // 생성자 부분의 autowired 필요없음
public class MemoController {

    private final MemoService memoService;
//    private final TimeStamped timeStamped;

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    // 글 생성
    @PostMapping("/api/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) { // dto를 문자열로 받아오겠다 ?
        return memoService.createMemo(requestDto);
    }

    // 전체 글 조회
    @GetMapping("/api/memos")
    public List<MemoResponseDto> getMemos() {
        return memoService.getMemos();
    }

    // 선택 글 조회
    @GetMapping("/api/memos/{id}")
    public MemoResponseDto getMemo(@PathVariable Long id/*url에서 입력한id*/) {
        return memoService.getMemo(id/*url에서 입력한id*/); // service의 getmemo메서드에 id값을 가져감
        // 마지막 단계 - service에서 리턴한 memo가 바로 memoService.getMemo(id)로 되어 출력
    }

    // 글 수정
    @PutMapping("/api/memos/{id}")
    public MemoResponseDto updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {// body에 dto를 json형식으로 받음
        return memoService.updateMemo(id, requestDto);
    }

    // 글 삭제
    @DeleteMapping("/api/memos/{id}")
    public String deleteMemo(@PathVariable Long id) {
        return memoService.deleteMemo(id);
    }



}
