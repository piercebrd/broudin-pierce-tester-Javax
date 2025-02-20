package com.parkit.parkingsystem.integration.config;

import com.parkit.parkingsystem.config.DataBaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Database configuration class for integration testing.

 * This class extends {@link DataBaseConfig} and provides methods to establish
 * and manage database connections specifically for test environments.

 */
public class DataBaseTestConfig extends DataBaseConfig {

    /** Logger instance for logging database operations related to tests. */

    private static final Logger logger = LogManager.getLogger("DataBaseTestConfig");

    /**
     * Establishes and returns a connection to the test database.
     *
     * @return A {@link Connection} object to interact with the test database.
     * @throws ClassNotFoundException If the JDBC driver is not found.
     * @throws SQLException If an error occurs while connecting to the database.
     */

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/test","root","pokju147");
    }

    /**
     * Closes an active database connection.
     *
     * @param con The {@link Connection} object to be closed.
     */

    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection",e);
            }
        }
    }

    /**
     * Closes a prepared statement.
     *
     * @param ps The {@link PreparedStatement} object to be closed.
     */

    public void closePreparedStatement(PreparedStatement ps) {
        if(ps!=null){
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement",e);
            }
        }
    }

    /**
     * Closes a result set.
     *
     * @param rs The {@link ResultSet} object to be closed.
     */

    public void closeResultSet(ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set",e);
            }
        }
    }
}
