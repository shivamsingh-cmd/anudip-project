-- ============================================
-- Hotel Management System - Database Schema
-- ============================================

CREATE DATABASE IF NOT EXISTS hotel_db;
USE hotel_db;

-- Users table (staff and admin)
CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50) UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,
    full_name   VARCHAR(100) NOT NULL,
    email       VARCHAR(100) UNIQUE NOT NULL,
    role        ENUM('ADMIN', 'RECEPTIONIST', 'MANAGER') DEFAULT 'RECEPTIONIST',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Rooms table
CREATE TABLE IF NOT EXISTS rooms (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    room_number  VARCHAR(10) UNIQUE NOT NULL,
    room_type    ENUM('STANDARD', 'DELUXE', 'SUITE', 'PENTHOUSE') NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    capacity     INT NOT NULL DEFAULT 2,
    status       ENUM('AVAILABLE', 'OCCUPIED', 'MAINTENANCE', 'RESERVED') DEFAULT 'AVAILABLE',
    amenities    TEXT,
    floor        INT NOT NULL,
    description  TEXT,
    image_url    VARCHAR(255)
);

-- Guests table
CREATE TABLE IF NOT EXISTS guests (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    first_name   VARCHAR(50) NOT NULL,
    last_name    VARCHAR(50) NOT NULL,
    email        VARCHAR(100) UNIQUE NOT NULL,
    phone        VARCHAR(20),
    address      TEXT,
    id_type      ENUM('PASSPORT', 'DRIVING_LICENSE', 'NATIONAL_ID', 'AADHAR') NOT NULL,
    id_number    VARCHAR(50) NOT NULL,
    nationality  VARCHAR(50),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    booking_ref     VARCHAR(20) UNIQUE NOT NULL,
    guest_id        INT NOT NULL,
    room_id         INT NOT NULL,
    check_in_date   DATE NOT NULL,
    check_out_date  DATE NOT NULL,
    adults          INT DEFAULT 1,
    children        INT DEFAULT 0,
    total_amount    DECIMAL(10, 2) NOT NULL,
    status          ENUM('CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT', 'CANCELLED') DEFAULT 'CONFIRMED',
    special_requests TEXT,
    booked_by       INT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (guest_id)  REFERENCES guests(id)   ON DELETE CASCADE,
    FOREIGN KEY (room_id)   REFERENCES rooms(id)    ON DELETE CASCADE,
    FOREIGN KEY (booked_by) REFERENCES users(id)    ON DELETE SET NULL
);

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    booking_id     INT NOT NULL,
    amount         DECIMAL(10, 2) NOT NULL,
    payment_method ENUM('CASH', 'CARD', 'UPI', 'BANK_TRANSFER', 'ONLINE') NOT NULL,
    payment_status ENUM('PENDING', 'COMPLETED', 'REFUNDED', 'FAILED') DEFAULT 'PENDING',
    transaction_id VARCHAR(100),
    paid_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
);

-- Services / Extras table
CREATE TABLE IF NOT EXISTS services (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2) NOT NULL,
    category    ENUM('FOOD', 'SPA', 'LAUNDRY', 'TRANSPORT', 'OTHER') NOT NULL
);

-- Booking services (many-to-many)
CREATE TABLE IF NOT EXISTS booking_services (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    booking_id   INT NOT NULL,
    service_id   INT NOT NULL,
    quantity     INT DEFAULT 1,
    total_price  DECIMAL(10, 2) NOT NULL,
    ordered_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id)  REFERENCES bookings(id)  ON DELETE CASCADE,
    FOREIGN KEY (service_id)  REFERENCES services(id)  ON DELETE CASCADE
);

-- ============================================
-- Seed Data
-- ============================================

-- Default admin user (password: Admin@123)
INSERT INTO users (username, password, full_name, email, role) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHWy', 'System Admin', 'admin@hotel.com', 'ADMIN'),
('receptionist1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHWy', 'Sarah Johnson', 'sarah@hotel.com', 'RECEPTIONIST'),
('manager1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHWy', 'Robert Chen', 'robert@hotel.com', 'MANAGER');

-- Rooms seed data
INSERT INTO rooms (room_number, room_type, price_per_night, capacity, status, amenities, floor, description) VALUES
('101', 'STANDARD', 2500.00, 2, 'AVAILABLE', 'WiFi, TV, AC, Mini Fridge', 1, 'Cozy standard room with garden view'),
('102', 'STANDARD', 2500.00, 2, 'AVAILABLE', 'WiFi, TV, AC, Mini Fridge', 1, 'Comfortable standard room'),
('103', 'STANDARD', 2800.00, 3, 'OCCUPIED', 'WiFi, TV, AC, Mini Fridge, Sofa', 1, 'Spacious standard room for families'),
('201', 'DELUXE', 4500.00, 2, 'AVAILABLE', 'WiFi, Smart TV, AC, Mini Bar, Bathtub, Balcony', 2, 'Luxury deluxe room with city view'),
('202', 'DELUXE', 4500.00, 2, 'RESERVED', 'WiFi, Smart TV, AC, Mini Bar, Bathtub, Balcony', 2, 'Elegant deluxe room with pool view'),
('203', 'DELUXE', 5000.00, 4, 'AVAILABLE', 'WiFi, Smart TV, AC, Mini Bar, Bathtub, Living Area', 2, 'Deluxe family room with panoramic view'),
('301', 'SUITE', 8500.00, 2, 'AVAILABLE', 'WiFi, 65" TV, AC, Full Bar, Jacuzzi, Living Room, Kitchen', 3, 'Premium suite with ocean view'),
('302', 'SUITE', 9000.00, 3, 'MAINTENANCE', 'WiFi, 65" TV, AC, Full Bar, Jacuzzi, Living Room, Dining Area', 3, 'Executive suite with private terrace'),
('401', 'PENTHOUSE', 25000.00, 6, 'AVAILABLE', 'WiFi, Home Theatre, AC, Full Bar, Private Pool, Kitchen, 3BR', 4, 'Spectacular penthouse with 360° views');

-- Services seed data
INSERT INTO services (name, description, price, category) VALUES
('Breakfast Buffet', 'Full breakfast buffet for one person', 650.00, 'FOOD'),
('Room Service (Lunch)', 'Lunch delivered to room', 800.00, 'FOOD'),
('Room Service (Dinner)', 'Dinner delivered to room', 950.00, 'FOOD'),
('Spa Treatment (60 min)', 'Full body massage and spa treatment', 3500.00, 'SPA'),
('Laundry Service', 'Laundry per kg', 150.00, 'LAUNDRY'),
('Airport Transfer', 'One-way airport pickup/drop', 1200.00, 'TRANSPORT'),
('Gym Access', 'Full day gym and fitness center access', 500.00, 'OTHER');

-- Sample guests
INSERT INTO guests (first_name, last_name, email, phone, address, id_type, id_number, nationality) VALUES
('Rahul', 'Sharma', 'rahul.sharma@email.com', '9876543210', '12, MG Road, Mumbai', 'AADHAR', '1234-5678-9012', 'Indian'),
('Priya', 'Patel', 'priya.patel@email.com', '9765432109', '45, Linking Road, Bandra, Mumbai', 'PASSPORT', 'Z1234567', 'Indian'),
('James', 'Wilson', 'james.wilson@email.com', '+44-7890123456', '10 Downing St, London', 'PASSPORT', 'GB123456', 'British');
