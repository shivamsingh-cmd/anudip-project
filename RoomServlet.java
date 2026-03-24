package com.hotel.servlet;

import com.hotel.dao.RoomDAO;
import com.hotel.model.Room;
import com.hotel.model.Room.RoomStatus;
import com.hotel.model.Room.RoomType;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * RoomServlet - Handles room listing, add, edit, delete, and status update.
 */
@WebServlet("/rooms/*")
public class RoomServlet extends HttpServlet {

    private final RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo(); // e.g., null, /add, /edit/5, /delete/5

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // LIST all rooms
                req.setAttribute("rooms", roomDAO.getAllRooms());
                req.getRequestDispatcher("/WEB-INF/views/rooms.jsp").forward(req, resp);

            } else if (pathInfo.equals("/add")) {
                req.getRequestDispatcher("/WEB-INF/views/room-form.jsp").forward(req, resp);

            } else if (pathInfo.startsWith("/edit/")) {
                int id = Integer.parseInt(pathInfo.split("/")[2]);
                req.setAttribute("room", roomDAO.getRoomById(id));
                req.getRequestDispatcher("/WEB-INF/views/room-form.jsp").forward(req, resp);

            } else if (pathInfo.startsWith("/delete/")) {
                int id = Integer.parseInt(pathInfo.split("/")[2]);
                roomDAO.deleteRoom(id);
                resp.sendRedirect(req.getContextPath() + "/rooms");

            } else if (pathInfo.startsWith("/status/")) {
                String[] parts = pathInfo.split("/");
                int id = Integer.parseInt(parts[2]);
                RoomStatus status = RoomStatus.valueOf(parts[3]);
                roomDAO.updateRoomStatus(id, status);
                resp.sendRedirect(req.getContextPath() + "/rooms");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Error: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/rooms.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idStr = req.getParameter("id");
        Room room = new Room();

        if (idStr != null && !idStr.isEmpty()) {
            room.setId(Integer.parseInt(idStr));
        }

        room.setRoomNumber(req.getParameter("roomNumber"));
        room.setRoomType(RoomType.valueOf(req.getParameter("roomType")));
        room.setPricePerNight(new BigDecimal(req.getParameter("pricePerNight")));
        room.setCapacity(Integer.parseInt(req.getParameter("capacity")));
        room.setStatus(RoomStatus.valueOf(req.getParameter("status")));
        room.setAmenities(req.getParameter("amenities"));
        room.setFloor(Integer.parseInt(req.getParameter("floor")));
        room.setDescription(req.getParameter("description"));
        room.setImageUrl(req.getParameter("imageUrl"));

        try {
            if (room.getId() == 0) {
                roomDAO.addRoom(room);
            } else {
                roomDAO.updateRoom(room);
            }
            resp.sendRedirect(req.getContextPath() + "/rooms");
        } catch (Exception e) {
            req.setAttribute("error", "Failed to save room: " + e.getMessage());
            req.setAttribute("room", room);
            req.getRequestDispatcher("/WEB-INF/views/room-form.jsp").forward(req, resp);
        }
    }
}
