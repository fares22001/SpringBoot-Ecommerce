package com.categoryservice.categoryservice.controller;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.categoryservice.categoryservice.repository.categoryrepo;
import com.categoryservice.categoryservice.model.Category;

@Controller
public class CategoryController {

    @Autowired
    private categoryrepo categoryRepository;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getcategory() {
        List<Category> category = this.categoryRepository.findAll();
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
    
    @PostMapping("/categories/add")
    public ResponseEntity<Category> createcategory(@RequestBody Map<String, String> body) {
        Category category = new Category();
        category.setName(body.get("name"));
        this.categoryRepository.save(category);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/categories/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") int id) {
    Optional<Category> categoryOptional=categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            categoryRepository.delete(categoryOptional.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
