package com.sparta.mymemo.repository;

import com.sparta.mymemo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();
    Optional<Post> findByIdAndUsername(Long id, String username); // 만약 find로 못찾으면 null로 반환 = Optional
}
