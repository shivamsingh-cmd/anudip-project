package com.hotel.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Booking - Represents a hotel room booking.
 */
public class Booking {

    public enum BookingStatus { CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED }

    private int           id;
    private String        bookingRef;
    private int           guestId;
    private int           roomId;
    private LocalDate     checkInDate;
    private LocalDate     checkOutDate;
    private int           adults;
    private int           children;
    private BigDecimal    totalAmount;
    private BookingStatus status;
    private String        specialRequests;
    private int           bookedBy;
    private LocalDateTime createdAt;

    // Joined fields (not stored in bookings table)
    private Guest  guest;
    private Room   room;
    private String bookedByName;

    // ---- Constructors ----
    public Booking() {}

    // ---- Business Methods ----

    /** Returns number of nights booked */
    public long getNights() {
        if (checkInDate != null && checkOutDate != null) {
            return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        }
        return 0;
    }

    /** Auto-calculates total from room price */
    public void calculateTotal(BigDecimal pricePerNight) {
        this.totalAmount = pricePerNight.multiply(BigDecimal.valueOf(getNights()));
    }

    // ---- Getters & Setters ----
    public int getId()                           { return id; }
    public void setId(int id)                    { this.id = id; }

    public String getBookingRef()                { return bookingRef; }
    public void setBookingRef(String r)          { this.bookingRef = r; }

    public int getGuestId()                      { return guestId; }
    public void setGuestId(int g)                { this.guestId = g; }

    public int getRoomId()                       { return roomId; }
    public void setRoomId(int r)                 { this.roomId = r; }

    public LocalDate getCheckInDate()            { return checkInDate; }
    public void setCheckInDate(LocalDate d)      { this.checkInDate = d; }

    public LocalDate getCheckOutDate()           { return checkOutDate; }
    public void setCheckOutDate(LocalDate d)     { this.checkOutDate = d; }

    public int getAdults()                       { return adults; }
    public void setAdults(int a)                 { this.adults = a; }

    public int getChildren()                     { return children; }
    public void setChildren(int c)               { this.children = c; }

    public BigDecimal getTotalAmount()           { return totalAmount; }
    public void setTotalAmount(BigDecimal t)     { this.totalAmount = t; }

    public BookingStatus getStatus()             { return status; }
    public void setStatus(BookingStatus s)       { this.status = s; }

    public String getSpecialRequests()           { return specialRequests; }
    public void setSpecialRequests(String r)     { this.specialRequests = r; }

    public int getBookedBy()                     { return bookedBy; }
    public void setBookedBy(int b)               { this.bookedBy = b; }

    public LocalDateTime getCreatedAt()          { return createdAt; }
    public void setCreatedAt(LocalDateTime t)    { this.createdAt = t; }

    public Guest getGuest()                      { return guest; }
    public void setGuest(Guest g)                { this.guest = g; }

    public Room getRoom()                        { return room; }
    public void setRoom(Room r)                  { this.room = r; }

    public String getBookedByName()              { return bookedByName; }
    public void setBookedByName(String n)        { this.bookedByName = n; }

    @Override
    public String toString() {
        return "Booking{ref='" + bookingRef + "', status=" + status +
               ", checkIn=" + checkInDate + ", checkOut=" + checkOutDate + '}';
    }
}
