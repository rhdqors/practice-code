package com.sparta.mymemo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SignupRequestDto {

    @NotBlank
    @Pattern(regexp = "^[0-9a-z]{4,10}$")
    private String username;

    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z]{8,15}$")
    private String password;

}
