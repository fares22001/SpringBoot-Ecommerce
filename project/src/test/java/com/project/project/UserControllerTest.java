package com.project.project;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.project.project.controller.UserController;

import com.project.project.model.User;
import com.project.project.repositories.UserRepositry;
import com.project.project.repositories.productRepo;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepositry userRepositry;

    @MockBean
    private productRepo repo;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterGet() throws Exception {
        mockMvc.perform(get("/Register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register.html"));
    }

    @Test
    public void testSaveUser() throws Exception {
        mockMvc.perform(post("/Register")
                .param("username", "testuser")
                .param("password", "password")
                .param("confirmPassword", "password")
                .flashAttr("user", new User()))
                .andExpect(status().isOk())
                .andExpect(view().name("login.html"));
    }

    @Test
    public void testLoginGet() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    // @Test
    // public void testLoginProcess() throws Exception {
    //     User user = new User();
    //     user.setUsername("testuser");
    //     user.setPassword("$2a$12$D4h/Vo4YyBp8lZZkzv3IiO7CfV6V4aN4M3T2JD1Gk/4O6sYuYjWJi"); // BCrypted password for "password"

    //     when(userRepositry.findByUsername("testuser")).thenReturn(user);

    //     mockMvc.perform(post("/User/Login")
    //             .param("username", "testuser")
    //             .param("password", "password"))
    //             .andExpect(status().isOk())
    //             .andExpect(view().name("index.html"));
    // }

    @Test
    public void testLogout() throws Exception {
        MvcResult result = mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andReturn();
    }
}
