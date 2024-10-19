package com.Final.Project.service;

import com.Final.Project.entity.Roles;
import com.Final.Project.repository.RolesDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RolesServiceImpl implements RolesService{

    @Autowired
    private RolesDao rolesDao;


    @Override
    //fetch or create admin role
    public Roles fetchCreateRole(String rolename){
        Roles adminRole = rolesDao.findByName(rolename.toUpperCase())
                .orElseGet(() -> {
                    Roles newRole = new Roles();
                    newRole.setName(rolename.toUpperCase());
                    newRole.setActive(true);
                    return rolesDao.save(newRole); // Save the new role to the database
                });
        return adminRole;
    }
}
