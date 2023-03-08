package com.sparta.mymemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseCodeDto {

    private String msg;
    private int statusCode;

    public ResponseCodeDto(String msg,int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
