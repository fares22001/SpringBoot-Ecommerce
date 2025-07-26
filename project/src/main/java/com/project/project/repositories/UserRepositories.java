package com.project.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.project.model.User;

public interface UserRepositories extends JpaRepository<User, Integer> {
    
    User findByUsername(String username);
}
