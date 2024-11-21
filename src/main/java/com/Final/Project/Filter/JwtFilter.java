package com.Final.Project.Filter;

import com.Final.Project.entity.Tokens;
import com.Final.Project.exception.ProjectIllegalArgumentException;
import com.Final.Project.repository.TokensDao;
import com.Final.Project.repository.UsersDao;
import com.Final.Project.service.JWTServiceImpl;
import com.Final.Project.entity.MyUserDetailService;
import com.Final.Project.service.TokenService;
import com.Final.Project.service.UsersService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {


    @Autowired
    private JWTServiceImpl jwtServiceImpl;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UsersService usersService;

    @Autowired
    private TokensDao tokensDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = extractTokenFromHeader(request);
        String refreshToken = extractTokenFromCookie(request);
        try {


            if (accessToken != null && refreshToken != null) {
                String usernameFromAccessToken = jwtServiceImpl.extractUserName(accessToken);
                String usernameFromRefreshToken = jwtServiceImpl.extractUserName(refreshToken);

                if (usernameFromAccessToken.equals(usernameFromRefreshToken)) {
                    Optional<Tokens> tokensOpt = tokensDao.findByUserId(usersDao.findByUserName(usernameFromAccessToken).get().getId());

                    if (tokensOpt.isPresent()) {
                        Tokens storedTokens = tokensOpt.get();
                        boolean accessTokenValid = jwtServiceImpl.validateToken(accessToken, storedTokens.getAccessToken());
                        if (!accessTokenValid) {
                            throw new ProjectIllegalArgumentException("Invalid Access Token", HttpStatus.UNAUTHORIZED);
                        }
                        boolean refreshTokenValid = jwtServiceImpl.validateToken(refreshToken, storedTokens.getRefreshToken());
                        boolean isUserActive = usersDao.findByUserName(usernameFromAccessToken).get().isActive();

                        if (isUserActive && accessTokenValid && refreshTokenValid && SecurityContextHolder.getContext().getAuthentication() == null) {
                            UserDetails userDetails = context.getBean(MyUserDetailService.class).loadUserByUsername(usernameFromAccessToken);
                            setSecurityContext(request, userDetails);
                        }
                    }
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            System.out.println("Hii the error is caught here");
            throw new ProjectIllegalArgumentException("Expired JWT Token", HttpStatus.UNAUTHORIZED);

        } catch (ProjectIllegalArgumentException e) {
            System.out.println("Hii the error is caught here in second catch");
            try{
                if (refreshToken != null && jwtServiceImpl.validateToken(refreshToken, refreshToken)) {
                    String isRefreshed = tokenService.handleRefreshToken(request,response);
                    return;
                }

                exceptionResolver.resolveException(request, response, null, e);

            }catch (ProjectIllegalArgumentException ex){
                exceptionResolver.resolveException(request, response, null, ex);

            }

        }
    }


    private void setSecurityContext(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private String extractTokenFromCookie(HttpServletRequest request) {

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}













//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String accessToken = extractTokenFromHeader(request);
//        String refreshToken = extractTokenFromCookie(request);
//
//        if (accessToken != null && refreshToken != null) {
//            System.out.println("Checking for access token and refresh token");
//            String usernameFromAccessToken = jwtServiceImpl.extractUserName(accessToken);
//            String usernameFromRefreshToken = jwtServiceImpl.extractUserName(refreshToken);
//
//            if (usernameFromAccessToken.equals(usernameFromRefreshToken)) {
//                Optional<Tokens> tokensOpt = tokensDao.findByUserId(usersDao.findByUserName(usernameFromAccessToken).get().getId());
//
//                if (tokensOpt.isPresent()) {
//                    Tokens storedTokens = tokensOpt.get();
//                    boolean accessTokenValid = jwtServiceImpl.validateToken(accessToken, storedTokens.getAccessToken());
//                    System.out.println("is token valid "+accessTokenValid);
//                    if(!accessTokenValid){
//                        System.out.println("Token expired");
//                        throw new ProjectIllegalArgumentException("Invalid Access Token", HttpStatus.UNAUTHORIZED);
//                    }
//                    boolean refreshTokenValid = jwtServiceImpl.validateToken(refreshToken, storedTokens.getRefreshToken());
//                    boolean isUserActive=usersDao.findByUserName(usernameFromAccessToken).get().isActive();
//
//                    if (isUserActive && accessTokenValid && refreshTokenValid && SecurityContextHolder.getContext().getAuthentication() == null) {
//                        UserDetails userDetails = context.getBean(MyUserDetailService.class).loadUserByUsername(usernameFromAccessToken);
//                        setSecurityContext(request, userDetails);
//                    }
//                }
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }