package com.Final.Project.repository;

import com.Final.Project.entity.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TokensDao extends JpaRepository<Tokens, Integer> {

    Optional<Tokens> findByUserId(int userId);
}

