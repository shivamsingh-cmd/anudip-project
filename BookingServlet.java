package com.hotel.servlet;

import com.hotel.dao.*;
import com.hotel.model.*;
import com.hotel.model.Booking.BookingStatus;
import com.hotel.model.Guest.IdType;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * BookingServlet - Handles booking creation, listing, check-in/out, and cancellation.
 */
@WebServlet("/bookings/*")
public class BookingServlet extends HttpServlet {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final RoomDAO    roomDAO    = new RoomDAO();
    private final GuestDAO   guestDAO   = new GuestDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                req.setAttribute("bookings", bookingDAO.getAllBookings());
                req.getRequestDispatcher("/WEB-INF/views/bookings.jsp").forward(req, resp);

            } else if (pathInfo.equals("/new")) {
                req.setAttribute("availableRooms", roomDAO.getAvailableRooms());
                req.getRequestDispatcher("/WEB-INF/views/booking-form.jsp").forward(req, resp);

            } else if (pathInfo.startsWith("/view/")) {
                int id = Integer.parseInt(pathInfo.split("/")[2]);
                req.setAttribute("booking", bookingDAO.getBookingById(id));
                req.getRequestDispatcher("/WEB-INF/views/booking-detail.jsp").forward(req, resp);

            } else if (pathInfo.startsWith("/checkin/")) {
                int id = Integer.parseInt(pathInfo.split("/")[2]);
                bookingDAO.updateBookingStatus(id, BookingStatus.CHECKED_IN);
                resp.sendRedirect(req.getContextPath() + "/bookings");

            } else if (pathInfo.startsWith("/checkout/")) {
                int id = Integer.parseInt(pathInfo.split("/")[2]);
                bookingDAO.updateBookingStatus(id, BookingStatus.CHECKED_OUT);
                resp.sendRedirect(req.getContextPath() + "/bookings");

            } else if (pathInfo.startsWith("/cancel/")) {
                int id = Integer.parseInt(pathInfo.split("/")[2]);
                bookingDAO.updateBookingStatus(id, BookingStatus.CANCELLED);
                resp.sendRedirect(req.getContextPath() + "/bookings");
            }

        } catch (Exception e) {
            req.setAttribute("error", "Error: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/bookings.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Step 1: Create or find guest
        String guestIdStr = req.getParameter("guestId");
        Guest guest;

        try {
            if (guestIdStr != null && !guestIdStr.isEmpty()) {
                guest = guestDAO.getGuestById(Integer.parseInt(guestIdStr));
            } else {
                // Create new guest
                guest = new Guest();
                guest.setFirstName(req.getParameter("firstName"));
                guest.setLastName(req.getParameter("lastName"));
                guest.setEmail(req.getParameter("email"));
                guest.setPhone(req.getParameter("phone"));
                guest.setAddress(req.getParameter("address"));
                guest.setIdType(IdType.valueOf(req.getParameter("idType")));
                guest.setIdNumber(req.getParameter("idNumber"));
                guest.setNationality(req.getParameter("nationality"));

                // Check if email already exists
                Guest existing = guestDAO.getGuestByEmail(guest.getEmail());
                if (existing != null) {
                    guest = existing;
                } else {
                    guestDAO.addGuest(guest);
                }
            }

            // Step 2: Create booking
            Booking booking = new Booking();
            booking.setGuestId(guest.getId());
            booking.setRoomId(Integer.parseInt(req.getParameter("roomId")));
            booking.setCheckInDate(LocalDate.parse(req.getParameter("checkInDate")));
            booking.setCheckOutDate(LocalDate.parse(req.getParameter("checkOutDate")));
            booking.setAdults(Integer.parseInt(req.getParameter("adults")));
            booking.setChildren(req.getParameter("children") != null
                                ? Integer.parseInt(req.getParameter("children")) : 0);
            booking.setSpecialRequests(req.getParameter("specialRequests"));

            // Calculate total
            Room room = roomDAO.getRoomById(booking.getRoomId());
            booking.calculateTotal(room.getPricePerNight());

            // Set who booked
            User loggedIn = (User) req.getSession().getAttribute("loggedInUser");
            booking.setBookedBy(loggedIn.getId());

            bookingDAO.createBooking(booking);
            resp.sendRedirect(req.getContextPath() + "/bookings/view/" + booking.getId());

        } catch (Exception e) {
            req.setAttribute("error", "Failed to create booking: " + e.getMessage());
            try {
                req.setAttribute("availableRooms", roomDAO.getAvailableRooms());
            } catch (Exception ex) { /* ignore */ }
            req.getRequestDispatcher("/WEB-INF/views/booking-form.jsp").forward(req, resp);
        }
    }
}
