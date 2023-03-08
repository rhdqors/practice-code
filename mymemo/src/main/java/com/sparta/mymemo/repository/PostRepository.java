package com.sparta.mymemo.repository;

import com.sparta.mymemo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();
//    Optional<Post> findAllByOrderByCreatedAtDesc(String username);
//    Optional<Post> findById(Long id);

}
