package com.hotel.dao;

import com.hotel.model.Guest;
import com.hotel.model.Guest.IdType;
import com.hotel.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GuestDAO - Handles all database operations for Guests.
 */
public class GuestDAO {

    public boolean addGuest(Guest guest) throws SQLException {
        String sql = "INSERT INTO guests (first_name, last_name, email, phone, address, " +
                     "id_type, id_number, nationality) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, guest.getFirstName());
            ps.setString(2, guest.getLastName());
            ps.setString(3, guest.getEmail());
            ps.setString(4, guest.getPhone());
            ps.setString(5, guest.getAddress());
            ps.setString(6, guest.getIdType().name());
            ps.setString(7, guest.getIdNumber());
            ps.setString(8, guest.getNationality());
            boolean result = ps.executeUpdate() > 0;
            if (result) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) guest.setId(keys.getInt(1));
                }
            }
            return result;
        }
    }

    public List<Guest> getAllGuests() throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) guests.add(mapRow(rs));
        }
        return guests;
    }

    public Guest getGuestById(int id) throws SQLException {
        String sql = "SELECT * FROM guests WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public Guest getGuestByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM guests WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean updateGuest(Guest guest) throws SQLException {
        String sql = "UPDATE guests SET first_name=?, last_name=?, email=?, phone=?, " +
                     "address=?, id_type=?, id_number=?, nationality=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, guest.getFirstName());
            ps.setString(2, guest.getLastName());
            ps.setString(3, guest.getEmail());
            ps.setString(4, guest.getPhone());
            ps.setString(5, guest.getAddress());
            ps.setString(6, guest.getIdType().name());
            ps.setString(7, guest.getIdNumber());
            ps.setString(8, guest.getNationality());
            ps.setInt(9, guest.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public int countGuests() throws SQLException {
        String sql = "SELECT COUNT(*) FROM guests";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private Guest mapRow(ResultSet rs) throws SQLException {
        Guest g = new Guest();
        g.setId(rs.getInt("id"));
        g.setFirstName(rs.getString("first_name"));
        g.setLastName(rs.getString("last_name"));
        g.setEmail(rs.getString("email"));
        g.setPhone(rs.getString("phone"));
        g.setAddress(rs.getString("address"));
        g.setIdType(IdType.valueOf(rs.getString("id_type")));
        g.setIdNumber(rs.getString("id_number"));
        g.setNationality(rs.getString("nationality"));
        return g;
    }
}
