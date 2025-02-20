package com.parkit.parkingsystem.constants;

/**
 * This class contains SQL query constants used for database operations
 * in the Parkit parking system.

 * It defines queries for retrieving, updating, and inserting parking and ticket data.

 * Note: This class should not be instantiated.
 */

public class DBConstants {

    /**
     * SQL query to retrieve the next available parking spot based on vehicle type.
     * The query selects the minimum parking number where the spot is available.
     */

    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";

    /**
     * SQL query to update the availability status of a parking spot.
     * It updates the "available" column based on the parking number.
     */

    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";

    /**
     * SQL query to insert a new parking ticket into the database.
     * It records the parking number, vehicle registration number, price,
     * and timestamps for entry and exit.
     */

    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";

    /**
     * SQL query to update the price and exit time of an existing parking ticket.
     */

    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";

    /**
     * SQL query to retrieve the most recent ticket for a given vehicle registration number.
     * It joins the ticket and parking tables to obtain parking details.
     * The query orders results by entry time and returns the most recent record.
     */

    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE, p.AVAILABLE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";

    /**
     * SQL query to count the number of tickets issued for a specific vehicle registration number.
     * This helps determine if a user is a recurring customer.
     */

    public static final String GET_NBTICKETS = "SELECT COUNT(*) FROM ticket WHERE VEHICLE_REG_NUMBER = ?";
}
