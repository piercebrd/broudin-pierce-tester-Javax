package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

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
        // Si la durée de stationnement est inférieure à 30 min, alors
        // je set le prix à 0.
        // Sinon, je rentre dans le switch
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

    public void calculateFare(Ticket ticket){
        calculateFare(ticket, false);
    }
}