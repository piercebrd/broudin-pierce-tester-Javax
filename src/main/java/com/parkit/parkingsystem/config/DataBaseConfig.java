package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Configuration class for database connections.
 * This class manages the creation and closing of database connections,
 * prepared statements, and result sets.
 */

public class DataBaseConfig {

    /** Logger instance for logging database operations. */
    private static final Logger logger = LogManager.getLogger("DataBaseConfig");

    /**
     * Establishes and returns a connection to the database.
     *
     * @return A {@link Connection} object to interact with the database.
     * @throws ClassNotFoundException If the JDBC driver is not found.
     * @throws SQLException If an error occurs while connecting to the database.
     */

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/prod","root","pokju147");
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
