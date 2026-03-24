package com.hotel.model;

import java.time.LocalDateTime;

/**
 * User - Represents a hotel staff member (Admin / Receptionist / Manager).
 */
public class User {

    public enum Role { ADMIN, RECEPTIONIST, MANAGER }

    private int           id;
    private String        username;
    private String        password;
    private String        fullName;
    private String        email;
    private Role          role;
    private LocalDateTime createdAt;

    public User() {}

    public User(String username, String password, String fullName,
                String email, Role role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email    = email;
        this.role     = role;
    }

    // ---- Getters & Setters ----
    public int getId()                       { return id; }
    public void setId(int id)                { this.id = id; }

    public String getUsername()              { return username; }
    public void setUsername(String u)        { this.username = u; }

    public String getPassword()              { return password; }
    public void setPassword(String p)        { this.password = p; }

    public String getFullName()              { return fullName; }
    public void setFullName(String n)        { this.fullName = n; }

    public String getEmail()                 { return email; }
    public void setEmail(String e)           { this.email = e; }

    public Role getRole()                    { return role; }
    public void setRole(Role r)              { this.role = r; }

    public LocalDateTime getCreatedAt()      { return createdAt; }
    public void setCreatedAt(LocalDateTime t){ this.createdAt = t; }

    public boolean isAdmin() { return this.role == Role.ADMIN; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role=" + role + '}';
    }
}
