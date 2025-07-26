package com.project.project.services;

import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.project.model.*;
import com.project.project.repositories.*;
@Service
public class cartservic {
    @Autowired
    private cartrepo cartrepo; 


 
   

    @SuppressWarnings("null")
    public Cart addItem(Cart item){

        cartrepo.save(item);
return item;
    }

    public void removeItem(int itemId) {
        cartrepo.deleteById(itemId);
    }

    public List<Cart> getAllItems() {
        return cartrepo.findAll();
    }

    public List<Cart> getItemsByUserId(int userId) {
        return cartrepo.findByUserId(userId);
    }
    public boolean doesItemExistForUser(int userId, int productId) {
        Cart wishlistItem =cartrepo.findByUserIdAndProductId(userId, productId);
        return wishlistItem != null;
    }

}
