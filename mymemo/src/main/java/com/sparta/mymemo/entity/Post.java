package com.sparta.mymemo.entity;

import com.sparta.mymemo.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Post extends TimeStamped{

    @Id // pk설정
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

//    @Column(nullable = false)
//    private String password;

//    @ManyToMany
//    private List<Post> postList = new ArrayList<>();

    public Post(PostRequestDto requestDto, User user) {
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
        this.username = user.getUsername();
    }

    public void updateMemo(PostRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
    }

}
