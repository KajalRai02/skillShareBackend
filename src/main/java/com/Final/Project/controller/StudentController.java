package com.Final.Project.controller;

import com.Final.Project.dto.CourseDTO;
import com.Final.Project.dto.UsersDTO;
import com.Final.Project.entity.Users;
import com.Final.Project.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/register")
    public ResponseEntity<String> createStudent(@RequestBody UsersDTO usersDTO){
        usersService.registerStudent(usersDTO);
        return ResponseEntity.ok("Successfully Resgistered");
    }

    //mapping to get the courses allocated to particular student.
    @GetMapping("/{id}")
    public ResponseEntity<List<CourseDTO>> getCourseByStudentId(@PathVariable int id) {
        List<CourseDTO> courseDTO = usersService.getAllotedCourses(id);
        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    //mapping to let student enroll course and add to it allocateddCourseId

    @PostMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<String> enrollCourse(@PathVariable int studentId, @PathVariable int courseId) {

        boolean isAdded = usersService.addCourseToStudent(studentId, courseId);

        if (isAdded) {
            return new ResponseEntity<>("Course successfully added to student.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to add course or course already allocated.", HttpStatus.BAD_REQUEST);
        }
    }



}
