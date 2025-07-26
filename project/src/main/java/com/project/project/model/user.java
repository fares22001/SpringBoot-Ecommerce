package com.project.project.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.*;;
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "User_id") // Specify the name of the primary key column
    private int id;
      

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{6,}$", message = "Password must contain at least one letter, one number, and be 6 characters or more")
    private String password;

    private String userrole;

 
      
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Cart> carts;





    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    public User() {
    }
 
   
   

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




    public int getId() {
        return id;
    }

    public String getUserrole() {
        return userrole;
    }
    
    
    
    
    public void setUserrole(String userrole) {
        this.userrole = userrole;
    }


    public void setId(int id) {
        this.id = id;
    }




    

}
