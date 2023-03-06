package com.sparta.mymemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoRequestDto {

    private String title;
    private String userName;
    private String password;
    private String contents;
}
