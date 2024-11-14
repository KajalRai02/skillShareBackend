package com.Final.Project.repository;


import com.Final.Project.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseDao extends JpaRepository<Course, Integer> {

    List<Course> findByCreatedBy(Integer createdBy);
}
