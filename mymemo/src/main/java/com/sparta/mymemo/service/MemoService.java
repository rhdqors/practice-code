package com.sparta.mymemo.service;

import com.sparta.mymemo.dto.MemoRequestDto;
import com.sparta.mymemo.dto.MemoResponseDto;
import com.sparta.mymemo.entity.Memo;
import com.sparta.mymemo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor // 추적
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional // 글 생성
    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        Memo memo = memoRepository.save(new Memo(requestDto));
        return new MemoResponseDto(memo);
    }

    @Transactional(readOnly = true) // 전체 글 조회
    public List<MemoResponseDto> getMemos() {
        return memoRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional // 선택 글 조회
    public MemoResponseDto getMemo(Long id/*controller에서 가져온 id값*/) {
        Memo memo = checkId(id); //아래 유효성검사 메소드
        return new MemoResponseDto(memo); // 아이디 일치하면 memo 리턴
    }

    @Transactional // 글 수정
    public MemoResponseDto updateMemo(Long id, MemoRequestDto requestDto) {
        Memo memo = checkId(id); //아래 유효성검사 메소드
        checkPw(memo, requestDto); //아래 유효성검사 메소드

        memo.updateMemo(requestDto); // 오류 없으면 수정해야할 메모 있다 판단. - 가지고온 데이터로 변경, memo entity에서 update메서드를 만들어 처리할거임
        return new MemoResponseDto(memo);
    }

    @Transactional // 글 삭제
    public String deleteMemo(Long id, MemoRequestDto requestDto) {
        Memo memo = checkId(id); //아래 유효성검사 메소드
        checkPw(memo, requestDto); //아래 유효성검사 메소드

        memoRepository.deleteById(id);
        return "삭제 완료.";
    }

    private Memo checkId(Long id) {
        return memoRepository.findById(id/*controller에서 가져온 id값*/).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")// 오류 발생시
        );
    }

    private void checkPw(Memo memo, MemoRequestDto requestDto) {
        if (!StringUtils.equals(memo.getPassword(), requestDto.getPassword())) { //
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // Objects.equals - null값이면 오류난다
        // StringUtils(Thymeleaf) - 다 된다
        // 아래 방식은 null이나 다른값이 올때 오류?.
//        if (!memo.getPassword().equals(requestDto.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
    }

}
