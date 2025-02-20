package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Integration tests for parking system database operations.

 * These tests ensure that:
 * - Vehicles are correctly parked and assigned a ticket.
 * - The parking system correctly processes vehicle exits.
 * - Recurring users receive the appropriate discount.
 * - The database updates parking spot availability correctly.

 */

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    /** Test database configuration. */

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    /** DAO for managing parking spot availability. */

    private static ParkingSpotDAO parkingSpotDAO;

    /** DAO for managing ticket records. */

    private static TicketDAO ticketDAO;

    /** Service for resetting test database entries. */

    private static DataBasePrepareService dataBasePrepareService;

    /** Mocked input reader utility for simulating user input. */

    @Mock
    private static InputReaderUtil inputReaderUtil;

    /**
     * Initializes test dependencies before all tests.
     *
     * @throws Exception If setup fails.
     */

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    /**
     * Prepares the test environment before each test execution.
     *
     * @throws Exception If setup fails.
     */

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    /**
     * Cleans up resources after all tests have been executed.
     */
    @AfterAll
    private static void tearDown(){

    }

    /**
     * Tests the parking process for a car.

     * Ensures that:
     * - A ticket is created in the database.
     * - The assigned parking spot is marked as unavailable.

     */

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        parkingService.processIncomingVehicle();

        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        assertNotNull("The ticket should be saved in the database", ticket);
        //assertEquals("Vehicle number should match", ticket.getVehicleRegNumber(), "ABCDEF");
        //assertNotNull("In-time should be recorded.", ticket.getInTime());
       // assertNull("Out-time should not be set yet", ticket.getOutTime());
        ParkingSpot assignedSpot = ticket.getParkingSpot();
        //assertNotNull("A parking spot should be set", assignedSpot);
        assertFalse("The assigned parking spot should be marked as unavailable", assignedSpot.isAvailable());

        //boolean isSpotAvailable = parkingSpotDAO.getNextAvailableSlot(assignedSpot.getParkingType()) != assignedSpot.getId();
        //assertTrue("Parking spot should be marked as unavailable in DB", isSpotAvailable);

        //TODO: check that a ticket is actually saved in DB and Parking table is updated with availability
    }

    /**
     * Tests the vehicle exit process from the parking lot.

     * Ensures that:
     * - The ticket's exit time is recorded.
     * - The correct fare is applied.
     * - The parking spot is marked as available after vehicle exit.

     *
     * @throws Exception If the test is interrupted.
     */

    @Test
    public void testParkingLotExit() throws Exception{
        testParkingACar();
        Thread.sleep(500);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();

        Thread.sleep(500);
        Ticket ticketFromDB = ticketDAO.getTicket("ABCDEF");

        assertNotNull("The ticket should still exist after exit processing", ticketFromDB);

        assertNotNull("Out-time should be recorded in the database.", ticketFromDB.getOutTime());

        if (ticketFromDB.getOutTime().getTime() - ticketFromDB.getInTime().getTime() < (30 * 60 * 1000)) {
            assertEquals("The fare should be zero for parking less than 30 minutes.", 0.0, ticketFromDB.getPrice());
        } else {
            assertTrue("The fare should be calculated and greater than zero.", ticketFromDB.getPrice() > 0);
        }

        ParkingSpot freedSpot = ticketFromDB.getParkingSpot();
        assertTrue("The parking spot should be available again after vehicle exit.", freedSpot.isAvailable());

        int availableStatus = parkingSpotDAO.getNextAvailableSlot(freedSpot.getParkingType());
        assertEquals("Parking spot should be available in DB", freedSpot.getId(), availableStatus);


        //TODO: check that the fare generated and out time are populated correctly in the database
    }

    /**
     * Tests the vehicle exit process for a recurring user.

     * Ensures that:
     * - A recurring user receives a 5% discount on parking fees.
     * - The discount is applied correctly after at least one previous visit.

     */

    @Test
    public void testParkingLotExitRecurringUser() {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        parkingService.processIncomingVehicle();
        parkingService.processExitingVehicle();
        Ticket firstTicket = ticketDAO.getTicket("ABCDEF");
        double normalFare = firstTicket.getPrice();

        parkingService.processIncomingVehicle();
        parkingService.processExitingVehicle();

        Ticket secondTicket = ticketDAO.getTicket("ABCDEF");
        double discountedFare = secondTicket.getPrice();

        int nbTickets = ticketDAO.getNbTicket("ABCDEF");
        assertTrue("The user should have at least 2 tickets to be considered recurring.", nbTickets > 1);

        double expectedFare = normalFare * 0.95;
        assertEquals(expectedFare, discountedFare, 0.01);


    }

}

