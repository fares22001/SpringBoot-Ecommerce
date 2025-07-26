package com.categoryservice.categoryservice.repository;

import com.categoryservice.categoryservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
  
public interface categoryrepo extends JpaRepository<Category, Integer>{

 }
