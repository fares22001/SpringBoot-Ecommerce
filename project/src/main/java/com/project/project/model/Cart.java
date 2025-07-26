package com.project.project.model;

 
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
  


@Entity
public class Cart {
    @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
      private int cart_id ;

 
    private int quantity;

@OneToOne(fetch = FetchType.LAZY )
@JoinColumn(name = "order_id")
    private OrderItem order;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private products product;

    @ManyToOne
    @JoinColumn(name="User_id")
    private User user ;
 

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public products getProduct() {
        return product;
    }

    public void setProduct(products product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }
   public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderItem getOrder() {
        return order;
    }

    public void setOrder(OrderItem order) {
        this.order = order;
    }



}
