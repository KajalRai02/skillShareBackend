package com.Final.Project.service;

import com.Final.Project.dto.CourseDTO;
import com.Final.Project.dto.UsersDTO;
import com.Final.Project.entity.Course;
import com.Final.Project.entity.Tokens;
import com.Final.Project.entity.Users;
import com.Final.Project.exception.ProjectIllegalArgumentException;
import com.Final.Project.mapper.CourseMapper;
import com.Final.Project.mapper.UsersMapper;
import com.Final.Project.repository.TokensDao;
import com.Final.Project.repository.UsersDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TokensDao tokensDao;


    //if role:student then no restriction.
    public UsersDTO registerStudent(UsersDTO usersDTO){
        if(usersDTO==null){
            throw new ProjectIllegalArgumentException("Cannot register.", HttpStatus.NO_CONTENT);
        }
        Users users= usersMapper.toEntity(usersDTO);
        users.setRoles(rolesService.fetchCreateRole("Student"));
        Users savedUsers = usersDao.save(users);
        return usersMapper.toDto(savedUsers);
    }

    //if role:admin then only the id with role:superadmin can create it
    public UsersDTO registerAdmin(UsersDTO usersDTO){
        if(usersDTO==null){
            throw  new ProjectIllegalArgumentException("Cannot register admin",HttpStatus.NO_CONTENT);
        }

        Users users= usersMapper.toEntity(usersDTO);

        // Set the role for the user
        users.setRoles(rolesService.fetchCreateRole("Admin"));
        Users savedUsers = usersDao.save(users);
        return usersMapper.toDto(savedUsers);
    }

    @Override
    public UsersDTO getUserById(int id) {
        Users user =usersDao.findById(id).orElseThrow(()->
            new ProjectIllegalArgumentException("User with id "+id+" not found",HttpStatus.NOT_FOUND)
        );
        return usersMapper.toDto(user);
    }

    @Override
    public List<UsersDTO> getAllUsers() {
        List<Users> users = usersDao.findAll();
        return users
                .stream()
                .map(usersMapper::toDto)
                .toList();
    }

    @Override
    public void deleteUserById(int id) {
        if (!usersDao.existsById(id)) {
            throw new ProjectIllegalArgumentException("The user doesn't exists.",HttpStatus.NOT_FOUND);
        }
        Optional<Tokens> optionalTokens = tokensDao.findByUserId(id);

        if (optionalTokens.isPresent()) {
            Tokens tokens = optionalTokens.get();

            tokensDao.deleteById(tokens.getId());
        } else {

            System.out.println("No token found for the user.");
        }
        System.out.println("id from service:"+id);
        usersDao.deleteById(id);

    }

    @Override
    public void updateStatus(int activeId, int id) {
//        if(usersDTO==null){
//            throw new ProjectIllegalArgumentException("no value provided",HttpStatus.NO_CONTENT);
//        }
        Users users = usersDao.findById(id).orElseThrow(()->
                new ProjectIllegalArgumentException("User with id "+id+" doesn't exist",HttpStatus.NOT_FOUND));
        if(activeId == 1){
            users.setActive(true);
        }else{
            users.setActive(false);
        }

        usersDao.save(users);
        System.out.println("Users isActive after save: " + users.isActive());
        //return "Status updated";

    }

    @Override
    public List<CourseDTO> getAllotedCourses(int id) {
        Users user = usersDao.findById(id).orElseThrow(() ->
                new ProjectIllegalArgumentException("User with id " + id + " not found", HttpStatus.NOT_FOUND)
        );

        Set<Course> courses = user.getAllocatedCourse();

        if (courses == null || courses.isEmpty()) {
            return List.of();
        }
        return courses
                .stream()
                .map(courseMapper::entityToDto)
                .toList();

//        return courses.stream()
//                .map(course -> new CourseDTO(course.getId(), course.getCourseName(), course.isActive()))
//                .toList();
    }



}
