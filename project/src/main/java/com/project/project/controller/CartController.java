package com.project.project.controller;

import java.util.List;
import java.util.Optional;

 
 
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
 import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.project.model.*;
 import com.project.project.repositories.cartrepo;
import com.project.project.repositories.productRepo;
import com.project.project.services.cartservic;
 
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;


@Controller
 public class CartController {

    @Autowired cartrepo cartrepo;
 
@Autowired 
private productRepo productRepo;
@Autowired
cartservic cartservic;

@GetMapping("/cart")
public ModelAndView getAll(HttpSession  HttpSession) {
    ModelAndView mav =new ModelAndView("cart.html");
   
    int currentUser = (Integer) HttpSession.getAttribute("User_id");
    List<Cart>cart=this.cartrepo.findByUserId(currentUser); 
    
    double totalPrice = cart.stream()
                                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                                .sum();
    mav.addObject("totalPrice", totalPrice);

    mav.addObject("Cart",cart);
    return mav;
}

@GetMapping("/remove/{cart_id}")
public ModelAndView deleteById(@PathVariable("cart_id") int cartItemId) {
    Optional<Cart> opcartitem=cartrepo.findById(cartItemId);
    Cart cartitem=opcartitem.get();
    products products=cartitem.getProduct();
    products.setQuantity(products.getQuantity()+cartitem.getQuantity());
    productRepo.save(products);
    
    this.cartservic.removeItem(cartItemId);
    return new ModelAndView("redirect:/cart");
}
 

@PostMapping("/updatecart/{cart_id}")
public ModelAndView updateCart(@Valid @ModelAttribute("Cart") Cart cart,
                               @PathVariable("cart_id") int id,
                               BindingResult result,
                               @RequestParam("quantity") int quantity) {

    Optional<Cart> optionalCart = cartrepo.findById(id);
    if (optionalCart.isPresent()) {
        Cart cartItem = optionalCart.get();
        int oldQuantity = cartItem.getQuantity();
        int difference = quantity - oldQuantity;

        products product = cartItem.getProduct();
        int availableQuantity = product.getQuantity();

        // Check if the new quantity is valid
        if (availableQuantity >= difference) {
            // Update the product quantity
            product.setQuantity(availableQuantity + oldQuantity - quantity);
            productRepo.save(product);

            // Update the cart item quantity
            cartItem.setQuantity(quantity);
            cartrepo.save(cartItem);
        } else {
            // Handle the case where the requested quantity exceeds available quantity
            // You can redirect to an error page or return an appropriate response
        }
    } else {
        // Handle the case where the cart item with the given ID is not found
        // You can redirect to an error page or return an appropriate response
    }

    return new ModelAndView("redirect:/cart");
}

  
   
}