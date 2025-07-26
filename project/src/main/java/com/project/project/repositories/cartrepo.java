package com.project.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.project.model.Cart;

@Repository
public interface cartrepo extends JpaRepository<Cart, Integer> {
 
   
    Cart findByUserIdAndProductId(int userId, int productId);

    void deleteByUserIdAndProductId(int userId, int productId);
     List<Cart> findByUserId(int userId);
}
