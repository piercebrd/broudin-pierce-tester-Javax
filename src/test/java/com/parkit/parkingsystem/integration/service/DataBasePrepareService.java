package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;

/**
 * Service class for preparing the test database.

 * This class is used to reset the test database by:
 * - Marking all parking spots as available.
 * - Clearing all ticket records.

 */

public class DataBasePrepareService {

    /** Configuration instance for connecting to the test database. */
    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    /**
     * Clears database entries before running integration tests.
     * This method:
     * - Resets the parking table by setting all spots to available.
     * - Clears the ticket table by truncating all entries.

     */

    public void clearDataBaseEntries(){
        Connection connection = null;
        try{
            connection = dataBaseTestConfig.getConnection();

            //set parking entries to available
            connection.prepareStatement("update parking set available = true").execute();

            //clear ticket entries;
            connection.prepareStatement("truncate table ticket").execute();

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }


}
