package edu.barishal.ghat;

public interface Refuelable {
    double getFuelLevel();

    void setFuelLevel(double fuelLevel);

    default void refuel(double liters) {
        if (!Double.isFinite(liters) || liters <= 0) {
            throw new IllegalArgumentException("Fuel amount must be greater than zero.");
        }
        setFuelLevel(getFuelLevel() + liters);
    }
}
