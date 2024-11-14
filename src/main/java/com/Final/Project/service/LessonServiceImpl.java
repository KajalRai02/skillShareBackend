package com.Final.Project.service;

import com.Final.Project.dto.LessonDTO;
import com.Final.Project.entity.Course;
import com.Final.Project.entity.Lesson;
import com.Final.Project.entity.UserPrincipal;
import com.Final.Project.exception.ProjectIllegalArgumentException;
import com.Final.Project.mapper.LessonMapper;
import com.Final.Project.repository.CourseDao;
import com.Final.Project.repository.LessonDao;
import com.Final.Project.utils.ServiceHelper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonDao lessonDao; // to perform crud operation on lesson entity.

    @Autowired
    private CourseDao courseDao;  //to perform crud operation on course entity.

    @Autowired
    private LessonMapper lessonMapper; //to map dto entity and vice versa

    @Autowired
    private ServiceHelper serviceHelper;

    public LessonDTO saveLesson(LessonDTO lessonDTO) {
        //validation1 : LessonDto should not contain empty or null lessonName.
        if (lessonDTO.getLessonName() == null || lessonDTO.getLessonName().trim().isEmpty()) {
            throw new ProjectIllegalArgumentException("Lesson name cannot be null or empty", HttpStatus.BAD_REQUEST);
        }

        // Validation 2: CourseId in lesson DTO should be valid and the course should exist.
        Integer courseId = lessonDTO.getCourseId();
        System.out.println("This is the course Id , i am trying to access: "+ courseId);
        if (courseId == null || courseId <= 0 || !courseDao.existsById(courseId)) {
            throw new ProjectIllegalArgumentException("Invalid Course ID: " + courseId,HttpStatus.BAD_REQUEST);
        }

        //convert dto to entity
        Lesson lesson = lessonMapper.dtoToEntity(lessonDTO);

        // Set the associated Course entity to the Lesson
        Course course = courseDao.findById(lessonDTO.getCourseId()).orElseThrow(() ->
                new ProjectIllegalArgumentException("Course with ID: " + lessonDTO.getCourseId() + " not found.",HttpStatus.NOT_FOUND));
        lesson.setCourse(course);


        //save it to database by using crud methods.
        Lesson savedLesson = lessonDao.save(lesson);

        //convert the entity back to dto and return it.
        return lessonMapper.entityToDto(savedLesson);

    }

    //read all data
    public List<LessonDTO> getAllLessons() {
        List<Lesson> lessons = lessonDao.findAll();
        return lessons
                .stream()
                .filter(Lesson::isActive)
                .map(lessonMapper::entityToDto)
                .toList();

    }

    //read data by id
    public LessonDTO getLessonById(int id) {
        //Validation 1: Fetch course by id or throw error
        Lesson lesson = lessonDao.findById(id).orElseThrow(() ->
                new ProjectIllegalArgumentException("The lesson you are trying to retrieve doesn't exists.",HttpStatus.NOT_FOUND));
        if(!lesson.isActive()){
            throw new ProjectIllegalArgumentException("The lesson you are trying to retrieve is inactive.",HttpStatus.BAD_REQUEST);
        }
        //convert entity to dto and return
        return lessonMapper.entityToDto(lesson);
    }

    //update by id
    public LessonDTO updateLessonById(int id, LessonDTO lessonDTO) {
        //Validation 1: Fetch the data if the id is valid.
        Lesson lesson = lessonDao.findById(id).orElseThrow(() ->
                new ProjectIllegalArgumentException("Cannot perform update. The given id doesn't exist.",HttpStatus.NOT_FOUND));

        //if lesson name is not empty, update it.
        if (lessonDTO.getLessonName() != null && !lessonDTO.getLessonName().trim().isEmpty()) {
            lesson.setLessonName(lessonDTO.getLessonName());
        }

        // if course id is not empty[can check for null later]
        if (lessonDTO.getCourseId() > 0) {
            Course course = courseDao.findById(lessonDTO.getCourseId()).orElseThrow(() ->
                    new ProjectIllegalArgumentException("Course with ID " + lessonDTO.getCourseId() + " does not exist.",HttpStatus.NOT_FOUND));
            lesson.setCourse(course);
        }

        // Save and return the updated lesson
        Lesson updatedLesson = lessonDao.save(lesson);
        return lessonMapper.entityToDto(updatedLesson); // Convert to DTO for return
    }

    //delete by id
    public void deleteLesson(int id) {
        //check if the lesson belong to its creator

        //retrieve the lesson entity from the lesson id.
        if (!lessonDao.existsById(id)) {
            throw new ProjectIllegalArgumentException("Lesson with lesson id " + id + " not found",HttpStatus.NOT_FOUND);
        }
        //delete lesson from lesson table
        lessonDao.deleteById(id);

    }
}

