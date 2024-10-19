package com.Final.Project.mapper;

import com.Final.Project.dto.RolesDTO;
import com.Final.Project.entity.Roles;
import org.springframework.stereotype.Component;

@Component
public class RolesMapper {

    public RolesDTO toDto(Roles roles){
        RolesDTO rolesDTO=new RolesDTO();
        rolesDTO.setId(roles.getId());
        rolesDTO.setName(roles.getName());
        return rolesDTO;
    }

    public Roles toEntity(RolesDTO rolesDTO){
        Roles roles=new Roles();
        roles.setId(rolesDTO.getId());
        roles.setName(rolesDTO.getName());
        roles.setActive(true);
        return roles;
    }
}
