package com.Final.Project.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokensDTO {

    private int id;
    private int userId;
    private String refreshToken;
    private String accessToken;
}
