package com.Final.Project.service;

import com.Final.Project.dto.CourseDTO;
import com.Final.Project.dto.UsersDTO;
import com.Final.Project.entity.Users;


import java.util.List;


public interface UsersService {
    UsersDTO registerStudent(UsersDTO usersDTO);

    UsersDTO registerAdmin(UsersDTO usersDTO);

    UsersDTO getUserById(int id);

    List<UsersDTO> getAllUsers();

    void deleteUserById(int id);

    void updateStatus(int activeId, int id);

    List<CourseDTO> getAllotedCourses(int id);

    boolean addCourseToStudent(int studentId, int courseId);
}
