package com.Final.Project.repository;

import com.Final.Project.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonDao extends JpaRepository<Lesson,Integer> {
}
