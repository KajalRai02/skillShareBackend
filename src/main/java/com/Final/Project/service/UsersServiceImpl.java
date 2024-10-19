package com.Final.Project.service;

import com.Final.Project.dto.UsersDTO;
import com.Final.Project.entity.Users;
import com.Final.Project.exception.ProjectIllegalArgumentException;
import com.Final.Project.mapper.UsersMapper;
import com.Final.Project.repository.CourseDao;
import com.Final.Project.repository.UsersDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private UsersMapper usersMapper;


    //if role:student then no restriction.
    public UsersDTO registerStudent(UsersDTO usersDTO){
        if(usersDTO==null){
            throw new ProjectIllegalArgumentException("Cannot register.", HttpStatus.NO_CONTENT);
        }
        Users users= usersMapper.toEntity(usersDTO);
        users.setRoles(rolesService.fetchCreateRole("Student"));
        Users savedUsers = usersDao.save(users);
        return usersMapper.toDto(savedUsers);
    }

    //if role:admin then only the id with role:superadmin can create it
    public UsersDTO registerAdmin(UsersDTO usersDTO){
        if(usersDTO==null){
            throw  new ProjectIllegalArgumentException("Cannot register admin",HttpStatus.NO_CONTENT);
        }

        Users users= usersMapper.toEntity(usersDTO);

        // Set the role for the user
        users.setRoles(rolesService.fetchCreateRole("Admin"));
        Users savedUsers = usersDao.save(users);
        return usersMapper.toDto(savedUsers);
    }

    @Override
    public UsersDTO getUserById(int id) {
        Users user =usersDao.findById(id).orElseThrow(()->
            new ProjectIllegalArgumentException("User with id "+id+" not found",HttpStatus.NOT_FOUND)
        );
        return usersMapper.toDto(user);
    }

    @Override
    public List<UsersDTO> getAllUsers() {
        List<Users> users = usersDao.findAll();
        return users
                .stream()
                .map(usersMapper::toDto)
                .toList();
    }

    @Override
    public void deleteUserById(int id) {
        if (!usersDao.existsById(id)) {
            throw new ProjectIllegalArgumentException("The user doesn't exists.",HttpStatus.NOT_FOUND);
        }
        usersDao.deleteById(id);

    }

    @Override
    public String updateStatus(UsersDTO usersDTO, int id) {
        if(usersDTO==null){
            throw new ProjectIllegalArgumentException("no value provided",HttpStatus.NO_CONTENT);
        }
        Users users = usersDao.findById(id).orElseThrow(()->
                new ProjectIllegalArgumentException("User with id "+id+" doesn't exist",HttpStatus.NOT_FOUND));
        if(usersDTO.getActiveId()==1){
            users.setActive(true);
        }else{
            users.setActive(false);
        }

        usersDao.save(users);
        return "Status updated";

    }


}
