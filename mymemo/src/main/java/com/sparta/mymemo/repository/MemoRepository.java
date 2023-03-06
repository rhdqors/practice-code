package com.sparta.mymemo.repository;

import com.sparta.mymemo.dto.MemoResponseDto;
import com.sparta.mymemo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<MemoResponseDto> findAllByOrderByCreatedAtDesc();
//    List<Memo> findByContentsAndTitleAndUserNameaAndCreatedAt
//    여러개의 컬럼명을 가져오려면 위처럼 해야하는데 너무 비효율적
}
