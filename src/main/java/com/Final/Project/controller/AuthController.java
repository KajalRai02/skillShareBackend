package com.Final.Project.controller;

import com.Final.Project.dto.TokensDTO;
import com.Final.Project.dto.UsersDTO;
import com.Final.Project.service.AuthService;
import com.Final.Project.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsersDTO usersDTO, HttpServletResponse response){
        authService.verify(usersDTO,response);
        return ResponseEntity.ok("login Successful");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response){
        String result = tokenService.handleRefreshToken(request,response);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout( HttpServletRequest request, HttpServletResponse response){
        authService.Userlogout(request,response);
        return ResponseEntity.ok("The user is successfully logged out Successful");
    }


}
