package com.sparta.mymemo.entity;

import com.sparta.mymemo.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends TimeStamped{

    @Id // pk설정
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @Column(nullable = false) // ddl 생성시 not null 설정. - 데이터 생성시 null 못들어오게?
//    private String author;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String title;

//    @Column(nullable = false)
//    private String password;
//
    @Column(nullable = false)
    private String username;

    @JoinColumn(name = "user_id", referencedColumnName = "Id")
    private Long userId;

    @ManyToMany
    private List<Post> postList = new ArrayList<>();

    public Post(PostRequestDto requestDto, Long userId) {
//        this.author = requestDto.getAuthor();
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
        this.username = requestDto.getUsername();
//        this.password = requestDto.getPassword();
    }

    public void updateMemo(PostRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
    }

}
