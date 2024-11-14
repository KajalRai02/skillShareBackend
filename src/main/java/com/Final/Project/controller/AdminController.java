package com.Final.Project.controller;

import com.Final.Project.dto.UsersDTO;
import com.Final.Project.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/superAdmins")
public class AdminController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public UsersDTO createAdministrator(@RequestBody UsersDTO usersDTO){
        return usersService.registerAdmin(usersDTO);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UsersDTO> getUserById(@PathVariable int id){
        UsersDTO usersDTO= usersService.getUserById(id);
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<UsersDTO>> getAllUsers(){
        List<UsersDTO> usersDTOList = usersService.getAllUsers();
        return  new ResponseEntity<>(usersDTOList,HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void>deleteUser(@PathVariable int id){
        System.out.println("The user i am trying to delete"+id);
        usersService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String>UpdateAdminStatus(@RequestBody Map<String,Integer> requestBody, @PathVariable int id){

        if (requestBody.containsKey("activeId")) {
            int activeId = requestBody.get("activeId");
            usersService.updateStatus(activeId,id);


        } else {
            System.out.println("activeId not found in request body"+requestBody);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        //usersService.updateStatus(usersDTO,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
