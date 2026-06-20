package org.guvi.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class Users {

    @Id
    private String id;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    @Indexed(unique = true) // 🔥 IMPORTANT
    private String email;

    private Boolean active;

    @NotBlank(message = "Password must not be blank")
    private String passwordHash;

    public Users(String id, String name, String email, Boolean active, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.active = active;
        this.passwordHash = passwordHash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
