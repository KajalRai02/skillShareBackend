package com.Final.Project.service;

import com.Final.Project.dto.UsersDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    UsersDTO verify(UsersDTO usersDTO, HttpServletResponse response);

    //void Userlogout(UsersDTO usersDTO, HttpServletResponse response);

    void Userlogout(HttpServletRequest request, HttpServletResponse response);
}
