package com.project.project.Repositorytest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import com.project.project.controller.productcontrollerv1;
import com.project.project.model.products;
import com.project.project.repositories.UserRepositories;
import com.project.project.repositories.UserRepositry;
 import com.project.project.repositories.productRepo;
import com.project.project.services.cartservic;
import static org.mockito.ArgumentMatchers.anyInt;
import java.io.IOException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import com.project.project.model.Cart;
import com.project.project.model.Category;
import com.project.project.model.User;
import com.project.project.model.products;
 import com.project.project.repositories.productRepo;
import com.project.project.services.cartservic;

import jakarta.servlet.http.HttpSession;

@SpringBootTest
@AutoConfigureMockMvc
public class productRepositoryTest {
    @Mock
    private productRepo productRepo;
 
    @Mock
    private cartservic cartservic;

    @InjectMocks
    private productcontrollerv1 productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProduct() throws IOException {
        BindingResult result = mock(BindingResult.class);
        MockMultipartFile[] multipartFiles = { new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[0]) };

        products product = new products();
        when(productRepo.save(any(products.class))).thenReturn(product);

        ModelAndView mav = productController.saveProduct(product, result, multipartFiles);

        assertEquals("redirect:/admin/products", mav.getViewName());
        verify(productRepo, times(1)).save(any(products.class));
    }

    @Test
    void testSaveProduct_WithInvalidData() throws IOException {
        // Mock BindingResult to simulate validation errors
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true); // Simulate validation errors

        // Create an empty array for MultipartFile since the focus is on validation
        // errors
        MockMultipartFile[] multipartFiles = {};

        // Create a product instance with invalid data
        products product = new products();
        // Optionally, set invalid data to the product object to simulate specific
        // validation failures

        // Call the saveProduct method
        ModelAndView mav = productController.saveProduct(product, result, multipartFiles);

        // Verify that the method redirects back to the add product page due to
        // validation errors
        assertEquals("addproduct.html", mav.getViewName());

        // Verify that the productRepo.save method is never called due to validation
        // errors
        verify(productRepo, never()).save(any(products.class));
    }

    @Test
    void testEditProduct() throws IOException {
        BindingResult result = mock(BindingResult.class);
        MockMultipartFile[] multipartFiles = { new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[0]) };

        products existingProduct = new products();
        when(productRepo.findById(anyInt())).thenReturn(existingProduct);

        ModelAndView mav = productController.editProduct(1, new products(), result, multipartFiles);

        assertEquals("redirect:/admin/products", mav.getViewName());
        verify(productRepo, times(1)).save(any(products.class));
    }

    @Test
    void testDeleteProduct() {
        products product = new products();
        when(productRepo.findById(anyInt())).thenReturn(product);

        RedirectView redirectView = productController.deleteProduct(1);

        assertEquals("/admin/products", redirectView.getUrl());
        verify(productRepo, times(1)).deleteById(1);
    }

    @Test
    void testAddItem() {
        BindingResult result = mock(BindingResult.class);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("User_id")).thenReturn(1);

        products product = new products();
        product.setQuantity(10);
        when(productRepo.findById(anyInt())).thenReturn(product);
        when(cartservic.doesItemExistForUser(anyInt(), anyInt())).thenReturn(false);

        Cart cartItem = new Cart();

        ModelAndView mav = productController.addItem(cartItem, result, 1, session);

        assertEquals("redirect:/cart", mav.getViewName());
        verify(cartservic, times(1)).addItem(any(Cart.class));
    }

}
