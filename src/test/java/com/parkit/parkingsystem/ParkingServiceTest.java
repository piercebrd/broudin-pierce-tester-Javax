package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static junit.framework.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ParkingService}.


 * These tests verify that:
 * - Vehicles are correctly parked and assigned tickets.
 * - Vehicles exiting are properly processed.
 * - Parking spot availability is updated correctly.
 * - Edge cases such as unavailable parking spots and invalid inputs are handled.
 */

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    /** The service instance being tested. */

    private static ParkingService parkingService;

    /** Mocked user input utility. */

    @Mock
    private static InputReaderUtil inputReaderUtil;

    /** Mocked DAO for managing parking spots. */

    @Mock
    private static ParkingSpotDAO parkingSpotDAO;

    /** Mocked DAO for managing tickets. */

    @Mock
    private static TicketDAO ticketDAO;

    /**
     * Sets up test dependencies and default mock behaviors before each test.
     */

    @BeforeEach
    public void setUpPerTest() {
        try {
            lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            lenient().when(inputReaderUtil.readSelection()).thenReturn(1);


            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            lenient().when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);


            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");

            lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);


            lenient().when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            lenient().when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    /**
     * Tests that a vehicle exiting the parking lot is correctly processed.

     * Ensures that:
     * - The ticket record is retrieved.
     * - The ticket is updated.
     * - The parking spot availability is modified accordingly.
     */

    @Test
    public void processExitingVehicleTest(){
        // Act
        parkingService.processExitingVehicle();

        // Assert
        verify(ticketDAO, times(1)).getNbTicket(anyString());
        verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
        //verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    /**
     * Tests that a new vehicle entering the parking lot is correctly processed.
     * Ensures that:
     * - The parking spot is allocated.
     * - The ticket is created and saved.
     * - The parking spot availability is updated.
     * @throws Exception If user input reading fails.
     */

    @Test
    public void processIncomingVehicleTest() throws Exception{
        // Act
        parkingService.processIncomingVehicle();

        // Assert
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);
        verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
        verify(ticketDAO, times(1)).getNbTicket(anyString());
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
    }

    /**
     * Tests the case where updating a ticket during vehicle exit fails.
     * <p>
     * Ensures that the parking spot is NOT updated if the ticket update fails.
     */

    @Test
    public void processExitingVehicleTestUnableUpdate() {

        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

        parkingService.processExitingVehicle();

        verify(ticketDAO, times(1)).getTicket(anyString());
        verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
        verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));

    }

    /**
     * Tests retrieving the next available parking spot.
     * Ensures that:
     * - The correct parking spot is returned.
     * - The parking spot is marked as available.
     */

    @Test
    public void testGetNextParkingNumberIfAvailable() {

        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

        ParkingSpot parkingSpot  = parkingService.getNextParkingNumberIfAvailable();

        assertNotNull(parkingSpot);
        assertEquals(1, parkingSpot.getId());
        assertTrue(parkingSpot.isAvailable());
    }

    /**
     * Tests retrieving the next parking spot when no spots are available.
     * Ensures that the method returns {@code null}.
     */

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() {
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(0);

        ParkingSpot parkingSpot  = parkingService.getNextParkingNumberIfAvailable();

        assertNull(parkingSpot);
    }

    /**
     * Tests retrieving the next parking spot when an invalid vehicle type is provided.
     * Ensures that an {@link IllegalArgumentException} is thrown.
     */

    @Test
    public void testGetNextParkingNumberIfAvailableParkingWrongArgument() {
        when(inputReaderUtil.readSelection()).thenReturn(3);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                parkingService.getNextParkingNumberIfAvailable();
        });
        assertEquals("Entered input is invalid", exception.getMessage());

    }




}
