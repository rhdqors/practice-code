package com.sparta.mymemo.repository;

import com.sparta.mymemo.dto.PostResponseDto;
import com.sparta.mymemo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<PostResponseDto> findAllByOrderByCreatedAtDesc();

    List<Post> findAllByUserId(Long userId);
//    List<Memo> findByContentsAndTitleAndUserNameaAndCreatedAt
//    여러개의 컬럼명을 가져오려면 위처럼 해야하는데 너무 비효율적



}
