package com.hotel.model;

import java.time.LocalDateTime;

/**
 * Guest - Represents a hotel guest entity.
 */
public class Guest {

    public enum IdType { PASSPORT, DRIVING_LICENSE, NATIONAL_ID, AADHAR }

    private int           id;
    private String        firstName;
    private String        lastName;
    private String        email;
    private String        phone;
    private String        address;
    private IdType        idType;
    private String        idNumber;
    private String        nationality;
    private LocalDateTime createdAt;

    // ---- Constructors ----
    public Guest() {}

    public Guest(String firstName, String lastName, String email,
                 String phone, String address, IdType idType,
                 String idNumber, String nationality) {
        this.firstName   = firstName;
        this.lastName    = lastName;
        this.email       = email;
        this.phone       = phone;
        this.address     = address;
        this.idType      = idType;
        this.idNumber    = idNumber;
        this.nationality = nationality;
    }

    // ---- Getters & Setters ----
    public int getId()                       { return id; }
    public void setId(int id)                { this.id = id; }

    public String getFirstName()             { return firstName; }
    public void setFirstName(String n)       { this.firstName = n; }

    public String getLastName()              { return lastName; }
    public void setLastName(String n)        { this.lastName = n; }

    public String getFullName()              { return firstName + " " + lastName; }

    public String getEmail()                 { return email; }
    public void setEmail(String e)           { this.email = e; }

    public String getPhone()                 { return phone; }
    public void setPhone(String p)           { this.phone = p; }

    public String getAddress()               { return address; }
    public void setAddress(String a)         { this.address = a; }

    public IdType getIdType()                { return idType; }
    public void setIdType(IdType t)          { this.idType = t; }

    public String getIdNumber()              { return idNumber; }
    public void setIdNumber(String n)        { this.idNumber = n; }

    public String getNationality()           { return nationality; }
    public void setNationality(String n)     { this.nationality = n; }

    public LocalDateTime getCreatedAt()      { return createdAt; }
    public void setCreatedAt(LocalDateTime t){ this.createdAt = t; }

    @Override
    public String toString() {
        return "Guest{id=" + id + ", name='" + getFullName() +
               "', email='" + email + "'}";
    }
}
