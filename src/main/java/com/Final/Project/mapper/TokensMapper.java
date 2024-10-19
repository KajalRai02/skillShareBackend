package com.Final.Project.mapper;

import com.Final.Project.dto.TokensDTO;
import com.Final.Project.entity.Tokens;
import org.springframework.stereotype.Component;

@Component
public class TokensMapper {

    public TokensDTO entityToDto(Tokens tokens){
        TokensDTO tokensDTO = new TokensDTO();
        tokensDTO.setId(tokens.getId());
        tokensDTO.setUserId(tokens.getUser().getId());
        tokensDTO.setRefreshToken(tokens.getRefreshToken());
        tokensDTO.setAccessToken(tokens.getAccessToken());

        return tokensDTO;
    }

    public Tokens DtoToEntity(TokensDTO tokensDTO){
        Tokens tokens = new Tokens();
        tokens.setId(tokensDTO.getId());
        // set this in service : tokens.setUser();
        tokens.setRefreshToken(tokensDTO.getRefreshToken());
        tokens.setAccessToken(tokensDTO.getAccessToken());
        return tokens;
    }
}
