package edu.barishal.ghat;

public class Speedboat extends PoweredVessel {
    public Speedboat(
            String vesselId,
            String name,
            int capacity,
            String route,
            double fuelLevel,
            String currentLocation) {
        super(vesselId, name, capacity, route, fuelLevel, currentLocation);
    }

    @Override
    public VesselType getVesselType() {
        return VesselType.SPEEDBOAT;
    }
}
