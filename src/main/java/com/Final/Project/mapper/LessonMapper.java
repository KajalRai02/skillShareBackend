package com.Final.Project.mapper;

import com.Final.Project.dto.LessonDTO;
import com.Final.Project.entity.Course;
import com.Final.Project.entity.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {

    public Lesson dtoToEntity(LessonDTO lessonDTO){
        Lesson lesson=new Lesson();
        lesson.setId(lessonDTO.getId());
        lesson.setLessonName(lessonDTO.getLessonName());
        //this below line of code is performed in service layer.so it also means that the lesson entity will be incomplete when
        //returned from the mapper. however since the logic is handled in service , it won't cause issue.

        //lesson.setCourse(course);
        lesson.setActive(true);

        return lesson;
    }


    public LessonDTO entityToDto(Lesson lesson){
        LessonDTO lessonDTO=new LessonDTO();
        lessonDTO.setId(lesson.getId());
        lessonDTO.setLessonName(lesson.getLessonName());
        if(lesson.getCourse() != null){
            lessonDTO.setCourseId(lesson.getCourse().getId());
        }
        lessonDTO.setActive(lesson.isActive());
        return lessonDTO;
    }
}
