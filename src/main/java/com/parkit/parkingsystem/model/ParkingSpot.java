package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * Represents a parking spot in the Parkit parking system.

 * Each parking spot has a unique number, a type (CAR or BIKE),
 * and an availability status.

 */


public class ParkingSpot {

    /** The unique number identifying the parking spot. */

    private int number;

    /** The type of vehicle that can use this parking spot (CAR or BIKE). */

    private ParkingType parkingType;

    /** Indicates whether the parking spot is available for use. */

    private boolean isAvailable;

    /**
     * Constructs a new {@link ParkingSpot} with the given parameters.
     *
     * @param number      The unique identifier for the parking spot.
     * @param parkingType The type of vehicle that can park here.
     * @param isAvailable Whether the parking spot is currently available.
     */

    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    /**
     * Gets the unique identifier (number) of the parking spot.
     *
     * @return The parking spot number.
     */

    public int getId() {
        return number;
    }

    /**
     * Sets the unique identifier (number) of the parking spot.
     *
     * @param number The new parking spot number.
     */

    public void setId(int number) {
        this.number = number;
    }

    /**
     * Gets the parking type of this spot (CAR or BIKE).
     *
     * @return The {@link ParkingType} of the spot.
     */

    public ParkingType getParkingType() {
        return parkingType;
    }

    /**
     * Sets the parking type of this spot.
     *
     * @param parkingType The new {@link ParkingType} for this spot.
     */

    public void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    /**
     * Checks if the parking spot is currently available.
     *
     * @return {@code true} if the spot is available, otherwise {@code false}.
     */

    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Sets the availability status of the parking spot.
     *
     * @param available {@code true} to mark the spot as available, otherwise {@code false}.
     */

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * Compares two {@link ParkingSpot} objects based on their number.
     *
     * @param o The object to compare with.
     * @return {@code true} if the parking spot numbers match, otherwise {@code false}.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    /**
     * Generates a hash code based on the parking spot number.
     *
     * @return The hash code of the parking spot.
     */

    @Override
    public int hashCode() {
        return number;
    }
}
