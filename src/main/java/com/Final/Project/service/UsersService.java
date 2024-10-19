package com.Final.Project.service;

import com.Final.Project.dto.UsersDTO;


import java.util.List;


public interface UsersService {
    UsersDTO registerStudent(UsersDTO usersDTO);

    UsersDTO registerAdmin(UsersDTO usersDTO);

    UsersDTO getUserById(int id);

    List<UsersDTO> getAllUsers();

    void deleteUserById(int id);

    String updateStatus(UsersDTO usersDTO, int id);

}
