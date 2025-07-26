package com.project.project.Repositorytest;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.project.controller.UserController;
import com.project.project.controller.adminController;
import com.project.project.model.User;
import com.project.project.repositories.UserRepositories;
import com.project.project.repositories.UserRepositry;

import jakarta.servlet.http.HttpSession;

public class adminRepositoryTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private UserRepositry userRepository;

    @InjectMocks
    private UserController userController;

    @Autowired
    private UserRepositories uRepositories;

    @Test
    public void testUsernameExistsInDatabase() {
        // Create a new user with an existing username
        User existingUser = new User();
        existingUser.setUsername("Fady");
        when(userRepository.findByUsername("Fady")).thenReturn(existingUser);
        existingUser.setPassword("password"); // Set a non-null password

        // Mock BindingResult and Model
        BindingResult bindingResult = mock(BindingResult.class);
        Model model = mock(Model.class);
        adminController adminController = new adminController(userRepository);
        // Call the saveUserByAdmin method of adminController
        ModelAndView modelAndView = adminController.saveUserByAdmin(existingUser, bindingResult, "password", "role",
                model);

        // Verify that the view name is "addUser"
        assertEquals("addUser", modelAndView.getViewName());

        // Verify that the userRepository.findByUsername method is called with the
        // correct username
        verify(userRepository, times(1)).findByUsername("Fady");

        // Check if the username exists in the database
        User userFromDatabase = userRepository.findByUsername("Fady");
        assertNotNull(userFromDatabase, "The username 'Fady' should exist in the database");
    }

    @Test
    public void testLogout() {
        // Mock HttpSession
        HttpSession session = mock(HttpSession.class);

        // Create an instance of UserController
        adminController aController = new adminController(userRepository);

        // Call the logout method
        ModelAndView modelAndView = aController.logout(session);

        // Verify that session.invalidate() is called
        verify(session).invalidate();

        // Verify that ModelAndView redirects to the login page ("/")
        assert modelAndView.getViewName().equals("redirect:/");
    }

    @Test
    public void testDeleteUser() {
        // Mock HttpSession
        HttpSession session = mock(HttpSession.class);

        // Create a sample user ID
        int userId = 1;

        // Mock UserRepository
        UserRepositry userRepository = mock(UserRepositry.class);

        // Mock behavior for findById method to return a non-null user when called with
        // userId
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        // Create an instance of adminController with userRepository
        adminController aController = new adminController(userRepository);

        // Call the deleteUser method
        ModelAndView modelAndView = aController.deleteUser(userId, session);

        // Verify that userRepository.findById(id) is called with the correct user ID
        verify(userRepository).findById(userId);

        // Verify that userRepository.delete(user) is called if the user is present
        verify(userRepository).delete(any());

        // Verify that ModelAndView redirects to the viewUsers page ("/admin/viewUsers")
        assertEquals("redirect:/admin/viewUsers", modelAndView.getViewName());
    }

    @Test
public void testUpdateUser_UserExists() {
    // Mock HttpSession
    HttpSession session = mock(HttpSession.class);

    // Create a sample user ID
    int userId = 1;

    // Mock existing user
    User existingUser = new User();
    existingUser.setId(userId);
    existingUser.setUsername("existingUsername");
    existingUser.setEmail("existing@example.com");
    existingUser.setPassword("existingPassword");

    // Mock updated user
    User updatedUser = new User();
    updatedUser.setUsername("updatedUsername");
    updatedUser.setEmail("updated@example.com");
    updatedUser.setPassword("updatedPassword");

    // Mock UserRepository
    UserRepositry userRepository = mock(UserRepositry.class);

    // Mock behavior for findById method to return the existing user when called with userId
    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

    // Create RedirectAttributes mock
    RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

    // Create an instance of adminController with userRepository
    adminController aController = new adminController(userRepository);

    // Call the updateUser method
    ModelAndView modelAndView = aController.updateUser(userId, updatedUser, redirectAttributes, session);

    // Verify that userRepository.findById(id) is called with the correct user ID
    verify(userRepository).findById(userId);

    // Capture the argument passed to userRepository.save()
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(userCaptor.capture());
    User capturedUser = userCaptor.getValue();

    // Verify that the user's information is updated correctly
    assertEquals("updatedUsername", capturedUser.getUsername());
    assertEquals("updated@example.com", capturedUser.getEmail());
    assertEquals("updatedPassword", capturedUser.getPassword());

    // Verify that redirectAttributes.addFlashAttribute() is called with the success message
    verify(redirectAttributes).addFlashAttribute("successMessage", "User updated successfully!");

    // Verify that ModelAndView redirects to the viewUsers page ("/admin/viewUsers")
    assertEquals("redirect:/admin/viewUsers", modelAndView.getViewName());
}


@Test
    public void testUpdateUser_UserDoesNotExist() {
        // Mock HttpSession
        HttpSession session = mock(HttpSession.class);

        // Create a sample user ID
        int userId = 1;

        // Mock updated user
        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("updatedPassword");

        // Mock UserRepository
        UserRepositry userRepository = mock(UserRepositry.class);

        // Mock behavior for findById method to return an empty Optional when called with userId
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Create RedirectAttributes mock
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Create an instance of adminController with userRepository
        adminController aController = new adminController(userRepository);

        // Call the updateUser method
        ModelAndView modelAndView = aController.updateUser(userId, updatedUser, redirectAttributes, session);

        // Verify that userRepository.findById(id) is called with the correct user ID
        verify(userRepository).findById(userId);

        // Verify that userRepository.save() is not called
        verify(userRepository, never()).save(any(User.class));

        // Verify that ModelAndView redirects to the viewUsers page ("/admin/viewUsers")
        assertEquals("redirect:/admin/viewUsers", modelAndView.getViewName());
    }

}
