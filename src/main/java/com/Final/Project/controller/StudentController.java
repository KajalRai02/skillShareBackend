package com.Final.Project.controller;

import com.Final.Project.dto.UsersDTO;
import com.Final.Project.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/register")
    public UsersDTO createStudent(@RequestBody UsersDTO usersDTO){
        return usersService.registerStudent(usersDTO);
    }

}
