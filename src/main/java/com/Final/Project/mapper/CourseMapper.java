package com.Final.Project.mapper;

import com.Final.Project.dto.CourseDTO;
import com.Final.Project.dto.LessonDTO;
import com.Final.Project.entity.Course;
import com.Final.Project.entity.Lesson;
import com.Final.Project.entity.Users;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    public CourseDTO entityToDto(Course course){

        CourseDTO courseDTO=new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setCourseName(course.getCourseName());

        Set<LessonDTO>lessonDTOS=new HashSet<>();

        if(course.getLessons()!=null){
            for(Lesson lesson: course.getLessons()){
                LessonDTO lessonDTO=new LessonDTO();
                lessonDTO.setId(lesson.getId());
                lessonDTO.setLessonName(lesson.getLessonName());
                lessonDTO.setCourseId(lesson.getId());
                lessonDTO.setActive(true);
                lessonDTOS.add(lessonDTO);
            }
        }
        courseDTO.setLessons(lessonDTOS);

        if(course.getStudents()!=null){
            courseDTO.setStudentID(course
                    .getStudents()
                    .stream()
                            .map(Users::getId)
                            .collect(Collectors.toSet())
                    );
        }

        courseDTO.setActive(course.isActive());

        return  courseDTO;
    }

    public Course dtoToEntity(CourseDTO courseDTO){

        Course course=new Course();
        course.setId(courseDTO.getId());
        course.setCourseName(courseDTO.getCourseName());

        Set<Lesson> lessons=new HashSet<>();

        //Convert LessonDTO to lesson entities and set them to course
        if(courseDTO.getLessons()!=null){
            for(LessonDTO lessonDTO: courseDTO.getLessons()){
                Lesson lesson=new Lesson();
                lesson.setId(lessonDTO.getId());
                lesson.setLessonName(lessonDTO.getLessonName());
                lesson.setActive(true);
                //set the courses
                lesson.setCourse(course);
                lessons.add(lesson);
            }
        }
        course.setLessons(lessons);

        course.setActive(true);

        return course;
    }


}
