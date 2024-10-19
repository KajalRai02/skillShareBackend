package com.Final.Project.service;

import com.Final.Project.dto.CourseDTO;

import java.util.List;

public interface CourseService {

    CourseDTO saveCourse(CourseDTO courseDTO);
    List<CourseDTO> getAllCourses();
    CourseDTO getCourseById( int id);
    CourseDTO updateCourse(int id, CourseDTO courseDTO);
    void deleteCourse(int id);
    void updateCourseStatus(CourseDTO courseDTO, int id);
}
