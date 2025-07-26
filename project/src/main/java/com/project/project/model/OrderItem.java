package com.project.project.model;

import java.util.Date;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
 import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
 
@Entity 
 public class OrderItem {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    private String  address;
    @NotNull
    private String payment;
    private Date date;
    private int price;
    private String status;
      
    @ManyToOne
    @JoinColumn(name="User_id")
    private User user;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Cart> cartitem;

public int getId() {
    return id;
}

public void setId(int id) {
    this.id = id;
}

public String getAddress() {
    return address;
}

public void setAddress(String address) {
    this.address = address;
}

public String getPayment() {
    return payment;
}

public void setPayment(String payment) {
    this.payment = payment;
}

public Date getDate() {
    return date;
}

public void setDate(Date date) {
    this.date = date;
}

public int getPrice() {
    return price;
}

public void setPrice(int price) {
    this.price = price;
}

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

public User getUser() {
    return user;
}

public void setUser(User user) {
    this.user = user;
}

public List<Cart> getCartitem() {
    return cartitem;
}

public void setCartitem(List<Cart> cartitem) {
    this.cartitem = cartitem;
}


}
