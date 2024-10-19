package com.Final.Project.entity;

import com.Final.Project.repository.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UsersDao usersDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users users=usersDao.findByUserName(username). orElseThrow(()->
                new UsernameNotFoundException("User not exists by Username or Email"));

        return new UserPrincipal(users);
    }
}
