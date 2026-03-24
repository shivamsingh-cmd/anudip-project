package com.hotel.dao;

import com.hotel.model.Room;
import com.hotel.model.Room.RoomStatus;
import com.hotel.model.Room.RoomType;
import com.hotel.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * RoomDAO - Handles all database operations for Rooms.
 */
public class RoomDAO {

    // ---- CREATE ----
    public boolean addRoom(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (room_number, room_type, price_per_night, " +
                     "capacity, status, amenities, floor, description, image_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType().name());
            ps.setBigDecimal(3, room.getPricePerNight());
            ps.setInt(4, room.getCapacity());
            ps.setString(5, room.getStatus().name());
            ps.setString(6, room.getAmenities());
            ps.setInt(7, room.getFloor());
            ps.setString(8, room.getDescription());
            ps.setString(9, room.getImageUrl());
            return ps.executeUpdate() > 0;
        }
    }

    // ---- READ ALL ----
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY room_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rooms.add(mapRow(rs));
            }
        }
        return rooms;
    }

    // ---- READ BY ID ----
    public Room getRoomById(int id) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    // ---- READ AVAILABLE ROOMS ----
    public List<Room> getAvailableRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status = 'AVAILABLE' ORDER BY room_type, price_per_night";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) rooms.add(mapRow(rs));
        }
        return rooms;
    }

    // ---- READ BY TYPE ----
    public List<Room> getRoomsByType(RoomType type) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE room_type = ? ORDER BY room_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rooms.add(mapRow(rs));
            }
        }
        return rooms;
    }

    // ---- UPDATE ----
    public boolean updateRoom(Room room) throws SQLException {
        String sql = "UPDATE rooms SET room_number=?, room_type=?, price_per_night=?, " +
                     "capacity=?, status=?, amenities=?, floor=?, description=?, image_url=? " +
                     "WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType().name());
            ps.setBigDecimal(3, room.getPricePerNight());
            ps.setInt(4, room.getCapacity());
            ps.setString(5, room.getStatus().name());
            ps.setString(6, room.getAmenities());
            ps.setInt(7, room.getFloor());
            ps.setString(8, room.getDescription());
            ps.setString(9, room.getImageUrl());
            ps.setInt(10, room.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // ---- UPDATE STATUS ----
    public boolean updateRoomStatus(int roomId, RoomStatus status) throws SQLException {
        String sql = "UPDATE rooms SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, roomId);
            return ps.executeUpdate() > 0;
        }
    }

    // ---- DELETE ----
    public boolean deleteRoom(int id) throws SQLException {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ---- STATS ----
    public int countByStatus(RoomStatus status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rooms WHERE status = ?";
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
    private Room mapRow(ResultSet rs) throws SQLException {
        Room r = new Room();
        r.setId(rs.getInt("id"));
        r.setRoomNumber(rs.getString("room_number"));
        r.setRoomType(RoomType.valueOf(rs.getString("room_type")));
        r.setPricePerNight(rs.getBigDecimal("price_per_night"));
        r.setCapacity(rs.getInt("capacity"));
        r.setStatus(RoomStatus.valueOf(rs.getString("status")));
        r.setAmenities(rs.getString("amenities"));
        r.setFloor(rs.getInt("floor"));
        r.setDescription(rs.getString("description"));
        r.setImageUrl(rs.getString("image_url"));
        return r;
    }
}
