package com.Final.Project.service;

import com.Final.Project.dto.UsersDTO;
import com.Final.Project.repository.UsersDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthServiceImpl implements AuthService{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsersDao usersDao;


    @Override
    public void verify(UsersDTO usersDTO, HttpServletResponse response) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(usersDTO.getUserName(),usersDTO.getPassword()));

        if(authentication.isAuthenticated()){
            String username= usersDTO.getUserName();

            //generate new refresh token
            tokenService.createUpdateRefreshToken(username,response);


        }
    }

    @Override
    public void Userlogout(HttpServletRequest request, HttpServletResponse response) {
        String username= request.getUserPrincipal().getName();
        int id = usersDao.findByUserName(username).get().getId();
        System.out.println("The user id to be deleted ="+id);
        tokenService.deleteByUserId(id);
        SecurityContextHolder.clearContext();

    }



}
