package com.project.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.project.model.products;

@Repository
public interface productRepo extends JpaRepository<products, Integer> {

    products deleteById(int id);
    products findById(int id); // Change method name to findById
}
