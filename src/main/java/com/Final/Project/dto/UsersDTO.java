package com.Final.Project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UsersDTO {

    private int id;
    private String userName;
    private String email;
    private String password;
    private String roles;
    private boolean isActive;
    private int activeId;
    private Set<Integer>allocatedCourseId;
}
