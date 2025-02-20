package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;


/**
 * Service class responsible for managing parking operations.

 * This class handles:
 * - Assigning parking spots to incoming vehicles.
 * - Processing exiting vehicles, calculating fares, and updating parking availability.

 */

public class ParkingService {

    /** Logger instance for logging parking-related operations. */

    private static final Logger logger = LogManager.getLogger("ParkingService");

    /** Service responsible for fare calculation. */

    private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

    /** Utility for reading user input. */

    private InputReaderUtil inputReaderUtil;

    /** DAO for managing parking spot availability. */

    private ParkingSpotDAO parkingSpotDAO;

    /** DAO for managing tickets in the database. */

    private  TicketDAO ticketDAO;

    /**
     * Constructs a {@link ParkingService} with the necessary dependencies.
     *
     * @param inputReaderUtil Utility for reading user input.
     * @param parkingSpotDAO  DAO for managing parking spot availability.
     * @param ticketDAO       DAO for managing ticket records.
     */

    public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO){
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }

    /**
     * Processes a new incoming vehicle.

     * Allocates an available parking spot, records entry time,
     * and creates a new ticket in the database.

     */

    public void processIncomingVehicle() {
        try{
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if(parkingSpot !=null && parkingSpot.getId() > 0){
                String vehicleRegNumber = getVehicleRegNumber();

                int nbTickets = ticketDAO.getNbTicket(vehicleRegNumber);
                if (nbTickets > 0) {
                    System.out.println("Heureux de vous revoir ! En tant qu'utilisateur régulier" +
                            "de notre parking, vous allez obtenir une remise de 5%.");
                } else {
                    System.out.println("Bienvenue chez Parkit! Nous sommes heureux de vous accueillir");
                }

                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot);//allot this parking space and mark it's availability as false

                Date inTime = new Date();
                Ticket ticket = new Ticket();
                //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
                //ticket.setId(ticketID);
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0);
                ticket.setInTime(inTime);
                ticketDAO.saveTicket(ticket);
                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number:"+parkingSpot.getId());
                System.out.println("Recorded in-time for vehicle number:"+vehicleRegNumber+" is:"+inTime);
            }
        }catch(Exception e){
            logger.error("Unable to process incoming vehicle",e);
        }
    }

    /**
     * Reads and returns the vehicle registration number from user input.
     *
     * @return The vehicle's registration number.
     * @throws Exception If an error occurs while reading input.
     */

    private String getVehicleRegNumber() throws Exception {
        System.out.println("Please type the vehicle registration number and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    /**
     * Finds and returns the next available parking spot.
     *
     * @return The next available {@link ParkingSpot}, or {@code null} if no spots are available.
     * @throws IllegalArgumentException If an invalid vehicle type is provided.
     */

    public ParkingSpot getNextParkingNumberIfAvailable(){
        int parkingNumber=0;
        ParkingSpot parkingSpot = null;
        try{
            ParkingType parkingType = getVehicleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if(parkingNumber > 0){
                parkingSpot = new ParkingSpot(parkingNumber,parkingType, true);
            }else{
                throw new Exception("Error fetching parking number from DB. Parking slots might be full");
            }
        }catch(IllegalArgumentException ie){
            logger.error("Error parsing user input for type of vehicle", ie);
            throw ie;
        }catch(Exception e){
            logger.error("Error fetching next available parking slot", e);
        }
        return parkingSpot;
    }

    /**
     * Reads and returns the vehicle type from user input.
     *
     * @return The selected {@link ParkingType}.
     * @throws IllegalArgumentException If an invalid input is provided.
     */

    private ParkingType getVehicleType(){
        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch(input){
            case 1: {
                return ParkingType.CAR;
            }
            case 2: {
                return ParkingType.BIKE;
            }
            default: {
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
            }
        }
    }

    /**
     * Processes a vehicle exiting the parking lot.

     * Calculates the fare, updates the ticket, and marks the parking spot as available.

     */

    public void processExitingVehicle() {
        try{
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            Date outTime = new Date();
            ticket.setOutTime(outTime);

            boolean discount = ticketDAO.getNbTicket(vehicleRegNumber) > 1;

            fareCalculatorService.calculateFare(ticket, discount);


            if(ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);

                boolean updateSuccess = parkingSpotDAO.updateParking(parkingSpot);

                parkingSpotDAO.updateParking(parkingSpot);


                System.out.println("Please pay the parking fare:" + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
            }else{
                System.out.println("Unable to update ticket information. Error occurred");
            }
        }catch(Exception e){
            logger.error("Unable to process exiting vehicle",e);
        }
    }
}
