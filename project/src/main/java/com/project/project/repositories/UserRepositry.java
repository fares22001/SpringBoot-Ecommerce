package com.project.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.project.model.User;
 
@Repository
public interface UserRepositry extends JpaRepository<User, Integer> {

    User findByUsername(String username);
    User  findByEmail(String email);
}