package com.Final.Project.service;

import com.Final.Project.dto.TokensDTO;
import com.Final.Project.entity.Tokens;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface TokenService {

    void createUpdateRefreshToken(String username, HttpServletResponse response);

    String handleRefreshToken(HttpServletRequest request, HttpServletResponse response);

    void deleteByUserId(int userId);

    void updateTokenEntity(Tokens tokenEntity, TokensDTO updatedValues);

    String extractRefreshTokenFromCookies(HttpServletRequest request);

}
