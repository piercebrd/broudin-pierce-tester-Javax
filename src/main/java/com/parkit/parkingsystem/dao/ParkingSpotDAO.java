package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Data Access Object (DAO) for managing parking spots in the database.

 * This class provides methods to:
 * - Retrieve the next available parking slot for a given vehicle type.
 * - Update the availability of a parking spot.

 */

public class ParkingSpotDAO {

    /** Logger instance for logging database operations related to parking spots. */

    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

    /** Database configuration instance to handle connections and resources. */

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * Retrieves the next available parking spot for a given parking type.
     *
     * @param parkingType The type of parking spot required (CAR or BIKE).
     * @return The parking spot number if available, otherwise -1.
     */

    public int getNextAvailableSlot(ParkingType parkingType){
        Connection con = null;
        int result=-1;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, parkingType.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                result = rs.getInt(1);;
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return result;
    }

    /**
     * Updates the availability status of a parking spot.
     *
     * @param parkingSpot The {@link ParkingSpot} object containing the updated information.
     * @return {@code true} if the update was successful, otherwise {@code false}.
     */

    public boolean updateParking(ParkingSpot parkingSpot){
        //update the availability fo that parking slot
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);

            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());
            int updateRowCount = ps.executeUpdate();


            dataBaseConfig.closePreparedStatement(ps);
            return (updateRowCount == 1);
        }catch (Exception ex){
            logger.error("Error updating parking info",ex);
            return false;
        }finally {
            dataBaseConfig.closeConnection(con);
        }
    }

}
