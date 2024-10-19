package com.Final.Project.mapper;

import com.Final.Project.dto.UsersDTO;
import com.Final.Project.entity.Course;
import com.Final.Project.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class UsersMapper {

    private BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(12);

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private RolesMapper rolesMapper;

    public UsersDTO toDto(Users users){
        UsersDTO usersDTO=new UsersDTO();
        usersDTO.setId(users.getId());
        usersDTO.setEmail(users.getEmail());
        usersDTO.setUserName(users.getUserName());
        usersDTO.setRoles(users
                .getRoles().getName()
        );
        if(users.getAllocatedCourse()!=null){
            usersDTO.setAllocatedCourseId(users
                    .getAllocatedCourse()
                    .stream()
                            .map(Course::getId)
                            .collect(Collectors.toSet())
                    );
        }
        usersDTO.setActive(users.isActive());
        return usersDTO;
    }

    public Users toEntity(UsersDTO usersDTO){
        Users users=new Users();

        users.setId(usersDTO.getId());
        users.setUserName(usersDTO.getUserName());
        users.setEmail(usersDTO.getEmail());
        users.setPassword(encoder.encode(usersDTO.getPassword()));
        //we are not mapping created by,because the user is registered as admin, that doesn't mean it has course associated.
        users.setActive(true);
        return users;
    }
}
