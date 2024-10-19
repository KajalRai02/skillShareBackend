package com.Final.Project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CourseDTO {

    private int id; // Course ID
    private String courseName; // Course name
    private boolean isActive;
    private int activeId;
    private Set<LessonDTO> lessons; // Lessons associated with the course
    private Set<Integer>studentID;

}
