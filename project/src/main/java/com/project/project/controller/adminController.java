package com.project.project.controller;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.project.model.OrderItem;
import com.project.project.model.User;
import com.project.project.repositories.OrderItemrepo;
import com.project.project.repositories.UserRepositry;
import com.project.project.repositories.productRepo;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("admin")
public class adminController {

    @Autowired
    private UserRepositry userRepositry;

    @Autowired
    private productRepo productrepo;

    public adminController(UserRepositry userRepository) {
        this.userRepositry = userRepository;
    }

    @GetMapping("")
    public ModelAndView getAdminHome(HttpSession session) {
        ModelAndView model = new ModelAndView("adminDashboard.html");
        long userCount = userRepositry.count();
        long productCount = productrepo.count();
        // long orderCount = OrderItemrepo.count();

        // Add the user count to the model
        // model.addObject("orderCount", OrderItem);
        model.addObject("userCount", userCount);
        model.addObject("productCount", productCount);
        return model;
    }

    @GetMapping("/viewUsers")
    public ModelAndView getUsers(HttpSession session) {
        ModelAndView model = new ModelAndView("viewUsers.html");
        List<User> users = this.userRepositry.findAll();
        model.addObject("users", users);
        return model;
    }

    @PostMapping("/deleteUser/{User_id}")
    public ModelAndView deleteUser(@PathVariable("User_id") int id, HttpSession session) {
        userRepositry.findById(id).ifPresent(userRepositry::delete);
        return new ModelAndView("redirect:/admin/viewUsers");
    }

    @GetMapping("/updateUser/{User_id}")
    public ModelAndView showUpdateUserForm(@PathVariable("User_id") int id, HttpSession session) {
        ModelAndView model = new ModelAndView("updateUser.html");
        userRepositry.findById(id).ifPresent(user -> model.addObject("user", user));
        return model;
    }

    @PostMapping("/updateUser/{User_id}")
    public ModelAndView updateUser(@PathVariable("User_id") int id, User updatedUser,
            RedirectAttributes redirectAttributes, HttpSession session) {
        return userRepositry.findById(id)
                .map(user -> {
                    user.setUsername(updatedUser.getUsername());
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    userRepositry.save(user);
                    redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
                    return new ModelAndView("redirect:/admin/viewUsers");
                })
                .orElseGet(() -> new ModelAndView("redirect:/admin/viewUsers"));
    }

    @GetMapping("/controlPages")
    public ModelAndView ShowControlPage(HttpSession session) {
        return new ModelAndView("controlPages.html");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        if (session != null) {
            session.invalidate(); //
        }
        return new ModelAndView("redirect:/"); // Redirect to the login page
    }

    @GetMapping("/addUser")
    public ModelAndView showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return new ModelAndView("addUser");
    }

    @PostMapping("/addUser")
    public ModelAndView saveUserByAdmin(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam(value = "role", required = false) String role,
            Model model) {

        User existingUser = userRepositry.findByUsername(user.getUsername());
        if (existingUser != null) {
            model.addAttribute("usernameExists", "Username already exists");
            return new ModelAndView("addUser");
        }

        if (bindingResult.hasErrors()) {
            return new ModelAndView("addUser");
        }

        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordMismatch", "Passwords do not match");
            return new ModelAndView("addUser");
        }

        // Assign default role if not provided
        user.setUserrole(role == null || role.isEmpty() ? "r" : role);

        // Hash the password
        String encodedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        user.setPassword(encodedPassword);

        // Save the user
        userRepositry.save(user);

        return new ModelAndView("redirect:/admin/addUser");
    }

}
