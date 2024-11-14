package com.Final.Project.controller;

import com.Final.Project.dto.CourseDTO;
import com.Final.Project.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    //declare method to save data to database
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDTO> savingCourse(@RequestBody CourseDTO courseDTO) {
        CourseDTO courseDTO1 = courseService.saveCourse(courseDTO);
        return new ResponseEntity<>(courseDTO1, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> readCourseData() {
        List<CourseDTO> courseDTOS = courseService.getAllCourses();
        return new ResponseEntity<>(courseDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable int id) {
        CourseDTO courseDTO = courseService.getCourseById(id);
        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    //api to fetch all the courses of a particular admin.
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CourseDTO>> getAdminsCourse(@PathVariable int id) {
        System.out.println("the admin whose courses i need to retrieve = "+id);
        List<CourseDTO> courseDTOS = courseService.getCoursesByAdminId(id);
        return new ResponseEntity<>(courseDTOS, HttpStatus.OK);
    }



    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable int id, @RequestBody CourseDTO courseDTO) {
        CourseDTO courseDTO1 = courseService.updateCourse(id, courseDTO);
        return new ResponseEntity<>(courseDTO1, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletingCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/status/{id}")
    public ResponseEntity<Void> updateCourseStatus(@RequestBody Map<String,Integer> requestBody, @PathVariable int id){

        if (requestBody.containsKey("activeId")) {
            int activeId = requestBody.get("activeId");
            courseService.updateCourseStatus(activeId, id);


        } else {
            System.out.println("activeId not found in request body"+requestBody);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>(HttpStatus.OK);



    }
}