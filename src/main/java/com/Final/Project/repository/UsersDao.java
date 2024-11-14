package com.Final.Project.repository;

import com.Final.Project.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UsersDao extends JpaRepository<Users, Integer> {
    Optional<Users> findByUserName(String username);

    // Find a user by their ID
    Optional<Users> findById(int id);
}
