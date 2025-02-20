package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main entry point for the Parkit Parking System application.

 * This class initializes the application and launches the interactive shell interface.

 */

public class App {

    /** Logger instance for logging application startup. */

    private static final Logger logger = LogManager.getLogger("App");

    /**
     * The main method that starts the Parkit Parking System.
     *
     * @param args Command-line arguments (not used).
     */

    public static void main(String args[]){
        logger.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}
