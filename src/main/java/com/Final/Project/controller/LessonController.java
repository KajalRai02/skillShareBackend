package com.Final.Project.controller;

import com.Final.Project.dto.LessonDTO;
import com.Final.Project.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LessonDTO> saveLessons(@RequestBody LessonDTO lessonDTO){
        LessonDTO lessonDTO1 = lessonService.saveLesson(lessonDTO);
        return new ResponseEntity<>(lessonDTO1, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LessonDTO>> readLessonData(){
        List<LessonDTO> lessonDTOS=lessonService.getAllLessons();
        return new ResponseEntity<>(lessonDTOS,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> getLessonById(@PathVariable int id){
        LessonDTO lessonDTO= lessonService.getLessonById(id);
        return new ResponseEntity<>(lessonDTO, HttpStatus.OK);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LessonDTO> updateCourse(@PathVariable int id, @RequestBody LessonDTO lessonDTO){
        LessonDTO lessonDTO1=lessonService.updateLessonById(id,lessonDTO);
        return new ResponseEntity<>(lessonDTO1, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletingCourse(@PathVariable int id){
        lessonService.deleteLesson(id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }
}
