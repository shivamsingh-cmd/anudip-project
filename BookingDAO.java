package com.hotel.dao;

import com.hotel.model.Booking;
import com.hotel.model.Booking.BookingStatus;
import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.model.Room.RoomType;
import com.hotel.model.Room.RoomStatus;
import com.hotel.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * BookingDAO - Handles all database operations for Bookings.
 */
public class BookingDAO {

    private final GuestDAO guestDAO = new GuestDAO();
    private final RoomDAO  roomDAO  = new RoomDAO();

    // ---- CREATE ----
    public boolean createBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (booking_ref, guest_id, room_id, check_in_date, " +
                     "check_out_date, adults, children, total_amount, status, special_requests, booked_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Generate booking reference
        booking.setBookingRef("BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, booking.getBookingRef());
            ps.setInt(2, booking.getGuestId());
            ps.setInt(3, booking.getRoomId());
            ps.setDate(4, Date.valueOf(booking.getCheckInDate()));
            ps.setDate(5, Date.valueOf(booking.getCheckOutDate()));
            ps.setInt(6, booking.getAdults());
            ps.setInt(7, booking.getChildren());
            ps.setBigDecimal(8, booking.getTotalAmount());
            ps.setString(9, BookingStatus.CONFIRMED.name());
            ps.setString(10, booking.getSpecialRequests());
            ps.setInt(11, booking.getBookedBy());

            boolean result = ps.executeUpdate() > 0;

            if (result) {
                // Mark room as reserved
                roomDAO.updateRoomStatus(booking.getRoomId(), RoomStatus.RESERVED);
                // Get generated ID
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) booking.setId(keys.getInt(1));
                }
            }
            return result;
        }
    }

    // ---- READ ALL (with joins) ----
    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, g.first_name, g.last_name, g.email AS guest_email, " +
                     "r.room_number, r.room_type, u.full_name AS booked_by_name " +
                     "FROM bookings b " +
                     "JOIN guests g ON b.guest_id = g.id " +
                     "JOIN rooms r ON b.room_id = r.id " +
                     "LEFT JOIN users u ON b.booked_by = u.id " +
                     "ORDER BY b.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) bookings.add(mapRow(rs));
        }
        return bookings;
    }

    // ---- READ BY ID ----
    public Booking getBookingById(int id) throws SQLException {
        String sql = "SELECT b.*, g.first_name, g.last_name, g.email AS guest_email, " +
                     "r.room_number, r.room_type, u.full_name AS booked_by_name " +
                     "FROM bookings b " +
                     "JOIN guests g ON b.guest_id = g.id " +
                     "JOIN rooms r ON b.room_id = r.id " +
                     "LEFT JOIN users u ON b.booked_by = u.id " +
                     "WHERE b.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    // ---- READ TODAY'S CHECK-INS ----
    public List<Booking> getTodayCheckIns() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, g.first_name, g.last_name, g.email AS guest_email, " +
                     "r.room_number, r.room_type, u.full_name AS booked_by_name " +
                     "FROM bookings b " +
                     "JOIN guests g ON b.guest_id = g.id " +
                     "JOIN rooms r ON b.room_id = r.id " +
                     "LEFT JOIN users u ON b.booked_by = u.id " +
                     "WHERE b.check_in_date = CURDATE() AND b.status = 'CONFIRMED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) bookings.add(mapRow(rs));
        }
        return bookings;
    }

    // ---- UPDATE STATUS (Check-in / Check-out / Cancel) ----
    public boolean updateBookingStatus(int bookingId, BookingStatus newStatus) throws SQLException {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus.name());
            ps.setInt(2, bookingId);
            boolean result = ps.executeUpdate() > 0;

            if (result) {
                Booking b = getBookingById(bookingId);
                if (b != null) {
                    RoomStatus roomStatus;
                    switch (newStatus) {
                        case CHECKED_IN:  roomStatus = RoomStatus.OCCUPIED;   break;
                        case CHECKED_OUT: roomStatus = RoomStatus.AVAILABLE;  break;
                        case CANCELLED:   roomStatus = RoomStatus.AVAILABLE;  break;
                        default:          roomStatus = RoomStatus.RESERVED;   break;
                    }
                    roomDAO.updateRoomStatus(b.getRoomId(), roomStatus);
                }
            }
            return result;
        }
    }

    // ---- REVENUE STATS ----
    public double getTotalRevenue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM bookings WHERE status != 'CANCELLED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        }
        return 0;
    }

    public int countByStatus(BookingStatus status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    // ---- MAPPER ----
    private Booking mapRow(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setId(rs.getInt("id"));
        b.setBookingRef(rs.getString("booking_ref"));
        b.setGuestId(rs.getInt("guest_id"));
        b.setRoomId(rs.getInt("room_id"));
        b.setCheckInDate(rs.getDate("check_in_date").toLocalDate());
        b.setCheckOutDate(rs.getDate("check_out_date").toLocalDate());
        b.setAdults(rs.getInt("adults"));
        b.setChildren(rs.getInt("children"));
        b.setTotalAmount(rs.getBigDecimal("total_amount"));
        b.setStatus(BookingStatus.valueOf(rs.getString("status")));
        b.setSpecialRequests(rs.getString("special_requests"));
        b.setBookedByName(rs.getString("booked_by_name"));

        // Joined guest summary
        Guest g = new Guest();
        g.setId(rs.getInt("guest_id"));
        g.setFirstName(rs.getString("first_name"));
        g.setLastName(rs.getString("last_name"));
        g.setEmail(rs.getString("guest_email"));
        b.setGuest(g);

        // Joined room summary
        Room r = new Room();
        r.setId(rs.getInt("room_id"));
        r.setRoomNumber(rs.getString("room_number"));
        r.setRoomType(RoomType.valueOf(rs.getString("room_type")));
        b.setRoom(r);

        return b;
    }
}
