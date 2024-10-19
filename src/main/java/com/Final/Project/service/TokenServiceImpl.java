package com.Final.Project.service;

import com.Final.Project.dto.TokensDTO;
import com.Final.Project.entity.Tokens;
import com.Final.Project.entity.Users;
import com.Final.Project.exception.ProjectIllegalArgumentException;
import com.Final.Project.mapper.TokensMapper;
import com.Final.Project.mapper.UsersMapper;
import com.Final.Project.repository.TokensDao;
import com.Final.Project.repository.UsersDao;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class TokenServiceImpl implements TokenService{

    @Autowired
    private JWTServiceImpl jwtServiceImpl;

    @Autowired
    private TokensDao tokensDao;

    @Autowired
    private TokensMapper tokensMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersDao usersDao;


    @Override
    public void createUpdateRefreshToken(String username, HttpServletResponse response){

        String accessToken = jwtServiceImpl.generateAccessToken(username);
        String refreshToken = jwtServiceImpl.generateRefreshToken(username);

        Users users = usersDao.findByUserName(username).orElseThrow(()->
                new ProjectIllegalArgumentException("Username doesn't exists",HttpStatus.NOT_FOUND));

        Optional<Tokens> existingToken = tokensDao.findByUserId(users.getId());

        Tokens tokenEntity;
        if(existingToken.isPresent()){
            tokenEntity=existingToken.get();
        }else{
            tokenEntity= new Tokens();
            tokenEntity.setUser(users);
        }

        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setAccessToken(accessToken);

        tokensDao.save(tokenEntity);

        // set the access token in the response headers or body
        response.setHeader("Authorization","Bearer "+accessToken);

        // set the refresh token in HttpOnly Cookie
        Cookie refreshCookie = new Cookie("refresh-token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(1000*60*60*24*7);
        response.addCookie(refreshCookie);

    }

    @Override
    public String handleRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookies(request);

        if (refreshToken == null) {
            throw  new ProjectIllegalArgumentException("Refresh Token Not found. ",HttpStatus.NOT_FOUND);
        }

        String userName = jwtServiceImpl.extractUserName(refreshToken);

        Optional<Tokens> tokensOpt = tokensDao.findByUserId(usersDao.findByUserName(userName)
                .orElseThrow(() -> new ProjectIllegalArgumentException("User not found",HttpStatus.NOT_FOUND))
                .getId());

        if (tokensOpt.isPresent()) {
            Tokens storedTokens = tokensOpt.get();
            if (jwtServiceImpl.validateToken(refreshToken, storedTokens.getRefreshToken())) {
                String newAccessToken = jwtServiceImpl.generateAccessToken(userName);

                // Update only the access token
                TokensDTO updatedTokens = new TokensDTO();
                updatedTokens.setAccessToken(newAccessToken);

                updateTokenEntity(storedTokens, updatedTokens);

                // Set the new access token in the response header
                response.setHeader("Authorization", "Bearer " + newAccessToken);

                return "Token Refreshed";
            } else {
                throw  new ProjectIllegalArgumentException( "Invalid refresh token. Please log in again.",HttpStatus.UNAUTHORIZED);
            }
        }

        throw  new ProjectIllegalArgumentException("Invalid request. Please log in again.",HttpStatus.UNAUTHORIZED);
    }



    @Override
    public String extractRefreshTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw  new ProjectIllegalArgumentException("Cookies not found",HttpStatus.NOT_FOUND);
    }

    @Override
    public void updateTokenEntity(Tokens tokenEntity, TokensDTO updatedValues) {
        // Update only the fields that are not null in the DTO
        if (updatedValues.getAccessToken() != null) {
            tokenEntity.setAccessToken(updatedValues.getAccessToken());
        }
        if (updatedValues.getRefreshToken() != null) {
            tokenEntity.setRefreshToken(updatedValues.getRefreshToken());
        }
        tokensDao.save(tokenEntity);
    }


    @Override
    public void deleteByUserId(int userId) {

        Tokens tokens=tokensDao.findByUserId(userId).orElseThrow(()->
                new ProjectIllegalArgumentException("User doesn't exist in token table",HttpStatus.NOT_FOUND));

        tokensDao.deleteById(tokens.getId());
    }



}
