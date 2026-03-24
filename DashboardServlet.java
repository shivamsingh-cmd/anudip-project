package com.hotel.servlet;

import com.hotel.dao.*;
import com.hotel.model.Booking.BookingStatus;
import com.hotel.model.Room.RoomStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * DashboardServlet - Aggregates stats and forwards to dashboard view.
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final RoomDAO    roomDAO    = new RoomDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final GuestDAO   guestDAO   = new GuestDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Room stats
            req.setAttribute("totalRooms",       roomDAO.getAllRooms().size());
            req.setAttribute("availableRooms",   roomDAO.countByStatus(RoomStatus.AVAILABLE));
            req.setAttribute("occupiedRooms",    roomDAO.countByStatus(RoomStatus.OCCUPIED));
            req.setAttribute("maintenanceRooms", roomDAO.countByStatus(RoomStatus.MAINTENANCE));

            // Booking stats
            req.setAttribute("confirmedBookings", bookingDAO.countByStatus(BookingStatus.CONFIRMED));
            req.setAttribute("checkedInBookings", bookingDAO.countByStatus(BookingStatus.CHECKED_IN));
            req.setAttribute("todayCheckIns",     bookingDAO.getTodayCheckIns());

            // Guest & Revenue
            req.setAttribute("totalGuests",  guestDAO.countGuests());
            req.setAttribute("totalRevenue", bookingDAO.getTotalRevenue());

            // Recent bookings (first 10)
            req.setAttribute("recentBookings",
                bookingDAO.getAllBookings().stream().limit(10).collect(java.util.stream.Collectors.toList()));

        } catch (Exception e) {
            req.setAttribute("error", "Failed to load dashboard: " + e.getMessage());
        }

        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}
