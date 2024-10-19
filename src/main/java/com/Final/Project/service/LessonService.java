package com.Final.Project.service;

import com.Final.Project.dto.LessonDTO;

import java.util.List;

public interface LessonService {
    LessonDTO saveLesson(LessonDTO lessonDTO);
    List<LessonDTO> getAllLessons();
    LessonDTO getLessonById(int id);
    LessonDTO updateLessonById(int id, LessonDTO lessonDTO);
    void deleteLesson(int id);
}
