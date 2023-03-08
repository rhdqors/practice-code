package com.sparta.mymemo.entity;

import com.sparta.mymemo.dto.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

//    @OneToMany
//    List<Post> posts = new ArrayList<>();

    public User(SignupRequestDto signupRequestDto, UserRoleEnum role) {
        this.role = role;
        this.username = signupRequestDto.getUsername();
        this.password = signupRequestDto.getPassword();
    }

}
