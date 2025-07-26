package com.project.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.project.model.OrderItem;
public interface OrderItemrepo extends JpaRepository<OrderItem,Integer> {
    
}
