package com.project.project.model;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "products")
public class products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int id;

    @NotEmpty(message = "This name is required")
    private String name;

    @NotEmpty(message = "This field is required")
    private String brand;

    @NotNull(message = "This field is required")
    @Min(value = 0, message = "Price must be non-negative")
    private double price;

    @NotNull(message = "This field is required")
    @Min(value = 0, message = "Discount must be non-negative")
    private double regularDiscount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Cart> carts;

    @NotEmpty(message = "This field is required")
    private String description;

    @Min(value = 0, message = "Quantity must be non-negative")
    @NotNull(message = "This field is required")
    private int quantity;

    private List<String> imageFileName;

    private boolean available = true;

    @Column(name = "category_id")
    private int categoryId;

    // Getters and setters...

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRegularDiscount() {
        return regularDiscount;
    }

    public void setRegularDiscount(double regularDiscount) {
        this.regularDiscount = regularDiscount;
    }

    public Set<Cart> getCarts() {
        return carts;
    }

    public void setCarts(Set<Cart> carts) {
        this.carts = carts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<String> getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(List<String> imageFileName) {
        this.imageFileName = imageFileName;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Other getters and setters...
}
