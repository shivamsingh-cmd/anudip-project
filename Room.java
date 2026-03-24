package com.hotel.model;

import java.math.BigDecimal;

/**
 * Room - Represents a hotel room entity.
 */
public class Room {

    public enum RoomType   { STANDARD, DELUXE, SUITE, PENTHOUSE }
    public enum RoomStatus { AVAILABLE, OCCUPIED, MAINTENANCE, RESERVED }

    private int        id;
    private String     roomNumber;
    private RoomType   roomType;
    private BigDecimal pricePerNight;
    private int        capacity;
    private RoomStatus status;
    private String     amenities;
    private int        floor;
    private String     description;
    private String     imageUrl;

    // ---- Constructors ----
    public Room() {}

    public Room(int id, String roomNumber, RoomType roomType,
                BigDecimal pricePerNight, int capacity, RoomStatus status,
                String amenities, int floor, String description, String imageUrl) {
        this.id            = id;
        this.roomNumber    = roomNumber;
        this.roomType      = roomType;
        this.pricePerNight = pricePerNight;
        this.capacity      = capacity;
        this.status        = status;
        this.amenities     = amenities;
        this.floor         = floor;
        this.description   = description;
        this.imageUrl      = imageUrl;
    }

    // ---- Getters & Setters ----
    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public String getRoomNumber()               { return roomNumber; }
    public void setRoomNumber(String n)         { this.roomNumber = n; }

    public RoomType getRoomType()               { return roomType; }
    public void setRoomType(RoomType t)         { this.roomType = t; }

    public BigDecimal getPricePerNight()        { return pricePerNight; }
    public void setPricePerNight(BigDecimal p)  { this.pricePerNight = p; }

    public int getCapacity()                    { return capacity; }
    public void setCapacity(int c)              { this.capacity = c; }

    public RoomStatus getStatus()               { return status; }
    public void setStatus(RoomStatus s)         { this.status = s; }

    public String getAmenities()                { return amenities; }
    public void setAmenities(String a)          { this.amenities = a; }

    public int getFloor()                       { return floor; }
    public void setFloor(int f)                 { this.floor = f; }

    public String getDescription()              { return description; }
    public void setDescription(String d)        { this.description = d; }

    public String getImageUrl()                 { return imageUrl; }
    public void setImageUrl(String u)           { this.imageUrl = u; }

    public boolean isAvailable() {
        return this.status == RoomStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", roomNumber='" + roomNumber +
               "', type=" + roomType + ", price=" + pricePerNight +
               ", status=" + status + '}';
    }
}
