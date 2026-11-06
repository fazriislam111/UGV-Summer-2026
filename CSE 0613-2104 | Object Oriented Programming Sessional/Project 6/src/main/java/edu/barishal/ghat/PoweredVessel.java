package edu.barishal.ghat;

public abstract class PoweredVessel extends Vessel implements Refuelable, Trackable {
    protected PoweredVessel(
            String vesselId,
            String name,
            int capacity,
            String route,
            double fuelLevel,
            String currentLocation) {
        super(vesselId, name, capacity, route, fuelLevel, currentLocation);
    }
}
