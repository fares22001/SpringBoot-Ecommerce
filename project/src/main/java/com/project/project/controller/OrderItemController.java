package com.project.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.servlet.ModelAndView;

import com.project.project.model.Cart;
  import com.project.project.repositories.cartrepo;
import com.project.project.repositories.productRepo;
import com.project.project.services.cartservic;
import jakarta.servlet.http.HttpSession;

import java.util.List;
 import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class OrderItemController {
    @Autowired
    cartrepo cartrepo;
@Autowired
productRepo productRepo;
@Autowired    
cartservic cartservic;
    
 @GetMapping("/checkout")
 public ModelAndView getMethodName(HttpSession  HttpSession) {
     ModelAndView mav=new ModelAndView("checkout.html");
int userorder=(Integer) HttpSession.getAttribute("User_id");
List<Cart> cart=cartrepo.findByUserId(userorder);
mav.addObject("order", cart);

    return mav ;
 }
 @PostMapping("/placeorder")
 public ModelAndView postMethodName(@RequestBody String entity) {
     //TODO: process POST request
     return new ModelAndView("/index");
  }
 
    
}
