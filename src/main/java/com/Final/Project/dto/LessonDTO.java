package com.Final.Project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonDTO {

    private int id;
    private String lessonName;
    private int courseId;
    private boolean isActive;
}
