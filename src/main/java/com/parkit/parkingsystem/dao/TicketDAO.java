package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * Data Access Object (DAO) for managing parking tickets in the database.

 * This class provides methods to:
 * - Save a new parking ticket.
 * - Retrieve an existing ticket for a vehicle.
 * - Update a ticket with price and exit time.
 * - Count the number of tickets issued for a vehicle.

 */

public class TicketDAO {

    /** Logger instance for logging ticket-related database operations. */

    private static final Logger logger = LogManager.getLogger("TicketDAO");

    /** Database configuration instance to handle connections and resources. */

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * Saves a parking ticket to the database.
     *
     * @param ticket The {@link Ticket} object containing the ticket details.
     * @return {@code true} if the ticket was successfully saved, otherwise {@code false}.
     */

    public boolean saveTicket(Ticket ticket){
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            //ps.setInt(1,ticket.getId());
            ps.setInt(1,ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null)?null: (new Timestamp(ticket.getOutTime().getTime())) );
            return ps.execute();
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
            return false;
        }
    }

    /**
     * Retrieves the most recent ticket for a given vehicle registration number.
     *
     * @param vehicleRegNumber The vehicle's registration number.
     * @return A {@link Ticket} object if found, otherwise {@code null}.
     */

    public Ticket getTicket(String vehicleRegNumber) {
        Connection con = null;
        Ticket ticket = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1,vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int parkingNumber = rs.getInt(1);
                ParkingType parkingType = ParkingType.valueOf(rs.getString(6));
                boolean isAvailable = rs.getBoolean(7);

                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(parkingNumber, parkingType,isAvailable);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4));
                ticket.setOutTime(rs.getTimestamp(5));

            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
            return ticket;
        }
    }

    /**
     * Updates an existing parking ticket with price and exit time.
     *
     * @param ticket The {@link Ticket} object with updated details.
     * @return {@code true} if the update was successful, otherwise {@code false}.
     */

    public boolean updateTicket(Ticket ticket) {
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
            ps.setInt(3,ticket.getId());
            ps.execute();
            return true;
        }catch (Exception ex){
            logger.error("Error saving ticket info",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }

    /**
     * Counts the number of tickets issued for a given vehicle registration number.
     *
     * @param vehicleRegNumber The vehicle's registration number.
     * @return The total number of tickets issued for the vehicle.
     */

    public int getNbTicket(String vehicleRegNumber) {
        Connection con = null;
        int nbTicket = 0;

        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_NBTICKETS);

            ps.setString(1, vehicleRegNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nbTicket = rs.getInt(1);
            }

            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            logger.error("Error counting tickets for vehicle : " +  vehicleRegNumber,ex);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return nbTicket;
    }
}
