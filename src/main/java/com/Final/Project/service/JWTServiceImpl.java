package com.Final.Project.service;

import com.Final.Project.exception.ProjectIllegalArgumentException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTServiceImpl {

    @Value("${jwt.secret}")
    private String secretKey;



    private final long accessTokenValidity = 1000*60*25;
    private final long refreshTokenValidity = 1000*60*60*24*7;

//    public JWTServiceImpl() throws NoSuchAlgorithmException {
//        KeyGenerator keygen = KeyGenerator.getInstance("HmacSHA256");
//        SecretKey sk = keygen.generateKey();
//        this.secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
//    }


    public String generateAccessToken(String username){
        Map<String, Object> claims= new HashMap<>();
        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+accessTokenValidity))
                .and()
                .signWith(getKey())
                .compact();
    }

    //generate  refresh token
    public String generateRefreshToken(String username){
        Map<String, Object> claims= new HashMap<>();
        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+refreshTokenValidity))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {

        final Claims claims = extractAllClaims(token);

        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try{
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        }catch (ExpiredJwtException e) {
            System.out.println("The token has expired");
            // Handle expired token
            throw new ProjectIllegalArgumentException("Invalid Token", HttpStatus.UNAUTHORIZED);
            //throw new ProjectIllegalArgumentException("Expired JWT token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (JwtException e) {
            System.out.println("Relogginng the  error");
            throw new ProjectIllegalArgumentException("Invalid JWT token", HttpStatus.UNAUTHORIZED);

        }
    }



    public boolean validateToken(String token, String storedToken) {
        return token.equals(storedToken) && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
