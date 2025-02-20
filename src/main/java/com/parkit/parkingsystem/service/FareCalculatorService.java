package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;


/**
 * Service class for calculating parking fares.

 * This service determines the parking fee based on vehicle type,
 * duration of stay, and whether a discount applies.

 */

public class FareCalculatorService {

    /**
     * Calculates the fare for a given parking ticket.
     *
     * @param ticket   The {@link Ticket} object containing parking details.
     * @param discount {@code true} if a discount applies, otherwise {@code false}.
     * @throws IllegalArgumentException If the exit time is null or earlier than the entry time.
     */

    public void calculateFare(Ticket ticket, boolean discount){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inTimeMillis = ticket.getInTime().getTime();
        long outTimeMillis = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double durationInHours = (double) (outTimeMillis - inTimeMillis) / (1000 * 60 * 60);

        if (durationInHours < 0) {
            throw new IllegalArgumentException("Parking duration cannot be negative");
        }
        // If duration is less than 30 min, parking is free
        if (durationInHours < 0.5) {
            ticket.setPrice(0);
        }
        else {
            double price;

            switch (ticket.getParkingSpot().getParkingType()){
                case CAR: {
                    price = durationInHours * Fare.CAR_RATE_PER_HOUR;
                    break;
                }
                case BIKE: {
                    price = durationInHours * Fare.BIKE_RATE_PER_HOUR;
                    break;
                }
                default: throw new IllegalArgumentException("Unknown Parking Type");
            }

            if (discount) {
                price *= 0.95;
            }
            ticket.setPrice(price);
        }
    }

    /**
     * Calculates the fare for a parking ticket without a discount.
     *
     * @param ticket The {@link Ticket} object containing parking details.
     */

    public void calculateFare(Ticket ticket){
        calculateFare(ticket, false);
    }
}