package com.Final.Project.bootstrap;

import com.Final.Project.dto.UsersDTO;
import com.Final.Project.entity.Roles;
import com.Final.Project.entity.Users;
import com.Final.Project.mapper.UsersMapper;
import com.Final.Project.repository.RolesDao;
import com.Final.Project.repository.UsersDao;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RolesDao rolesDao;
    private final UsersDao usersDao;
    private final UsersMapper usersMapper;

    public AdminSeeder(RolesDao rolesDao, UsersDao usersDao,UsersMapper usersMapper) {
        this.rolesDao = rolesDao;
        this.usersDao = usersDao;
        this.usersMapper = usersMapper;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        this.createSuperAdministrator();

    }

    private void createSuperAdministrator() {

        UsersDTO usersDTO=new UsersDTO();
        usersDTO.setUserName("Super Admin");
        usersDTO.setEmail("super.admin@gmail.com");
        usersDTO.setPassword("123456");

        Optional<Roles> optionalRole = rolesDao.findByName("SUPER_ADMIN");
        Optional<Users> optionalUsers = usersDao.findByUserName(usersDTO.getUserName());

        if(!optionalRole.isEmpty() || optionalUsers.isPresent()){
            return;
        }

      //  usersDTO.setRoles("SUPER_ADMIN");

        Users users=usersMapper.toEntity(usersDTO);
        users.setRoles(new Roles(true,"SUPER_ADMIN"));
        usersDao.save(users);
    }

}
