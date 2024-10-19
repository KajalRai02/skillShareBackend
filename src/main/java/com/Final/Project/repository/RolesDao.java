package com.Final.Project.repository;

import com.Final.Project.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RolesDao extends JpaRepository<Roles,Integer> {
    Optional<Roles> findByName(String roleName);
}
