package com.parkit.parkingsystem.model;

import java.util.Calendar;
import java.util.Date;


/**
 * Represents a parking ticket in the Parkit parking system.

 * Each ticket contains information about the parking spot,
 * vehicle registration number, price, and timestamps for entry and exit.

 */

public class Ticket {

    /** Unique identifier for the ticket. */

    private int id;

    /** The parking spot associated with this ticket. */

    private ParkingSpot parkingSpot;

    /** The vehicle's registration number. */

    private String vehicleRegNumber;

    /** The total price charged for the parking duration. */

    private double price;

    /** The timestamp when the vehicle entered the parking lot. */

    private Date inTime;

    /** The timestamp when the vehicle exited the parking lot. */

    private Date outTime;

    /**
     * Gets the unique identifier of the ticket.
     *
     * @return The ticket ID.
     */

    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the ticket.
     *
     * @param id The new ticket ID.
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the parking spot associated with this ticket.
     *
     * @return The {@link ParkingSpot} object linked to this ticket.
     */

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    /**
     * Sets the parking spot for this ticket.
     *
     * @param parkingSpot The {@link ParkingSpot} to associate with this ticket.
     */

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    /**
     * Gets the vehicle's registration number.
     *
     * @return The vehicle registration number.
     */

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     * Sets the vehicle's registration number.
     *
     * @param vehicleRegNumber The vehicle registration number.
     */

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    /**
     * Gets the total price charged for parking.
     *
     * @return The parking fee.
     */

    public double getPrice() {
        return price;
    }

    /**
     * Sets the total price charged for parking.
     *
     * @param price The parking fee.
     */

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the entry time of the vehicle.
     *
     * @return The {@link Date} representing when the vehicle entered.
     */


    public Date getInTime() {
        return inTime;
    }

    /**
     * Sets the entry time of the vehicle.
     *
     * @param inTime The {@link Date} representing when the vehicle entered.
     */

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    /**
     * Gets the exit time of the vehicle.
     *
     * @return The {@link Date} representing when the vehicle exited.
     */

    public Date getOutTime() {
        return outTime;
    }

    /**
     * Sets the exit time of the vehicle.
     *
     * @param outTime The {@link Date} representing when the vehicle exited.
     */

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }
}
