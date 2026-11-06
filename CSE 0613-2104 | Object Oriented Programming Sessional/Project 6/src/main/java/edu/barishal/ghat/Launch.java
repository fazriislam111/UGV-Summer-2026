package edu.barishal.ghat;

public class Launch extends Vessel implements Refuelable, Trackable {
    public Launch(
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
        return VesselType.LAUNCH;
    }
}
