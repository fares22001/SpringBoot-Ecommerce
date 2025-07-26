package com.project.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.servlet.ModelAndView;

import com.project.project.model.Category;
import com.project.project.services.categoryservice;

import jakarta.validation.Valid;

@Controller
public class categoryController {

    @Autowired
    private categoryservice categoryservice;

    @GetMapping("/categories")
    public ModelAndView getAllCategories() {
        List<Category> categories = categoryservice.findAll();
        ModelAndView mav = new ModelAndView("AdminCategory.html");
        mav.addObject("categories", categories);
        return mav;
    }

    @GetMapping("/categories/add")
    public ModelAndView showCreateCategoryForm() {
        ModelAndView mav = new ModelAndView("addcategory.html");
        mav.addObject("category", new Category());
        return mav;
    }

    @PostMapping("/categories/add")
    public ModelAndView saveCategory(@Valid @ModelAttribute("category") Category category, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("addcategory.html", "formErrors", result.getAllErrors());
        }
        categoryservice.save(category);
        return new ModelAndView("redirect:/categories");
    }

    @DeleteMapping("/categories/delete/{id}")
    public ModelAndView deleteCategory(@PathVariable("id") int id) {
        categoryservice.delete(id);
        return new ModelAndView("redirect:/categories");
    }
}
