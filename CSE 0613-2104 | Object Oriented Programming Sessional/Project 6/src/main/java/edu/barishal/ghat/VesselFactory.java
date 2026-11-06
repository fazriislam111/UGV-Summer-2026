package edu.barishal.ghat;

public final class VesselFactory {
    private VesselFactory() {
    }

    public static Vessel create(
            VesselType type,
            String vesselId,
            String name,
            int capacity,
            String route,
            double fuelLevel,
            String currentLocation) {
        return switch (type) {
            case LAUNCH -> new Launch(
                    vesselId, name, capacity, route, fuelLevel, currentLocation);
            case SPEEDBOAT -> new Speedboat(
                    vesselId, name, capacity, route, fuelLevel, currentLocation);
            case TRAWLER -> new Trawler(
                    vesselId, name, capacity, route, fuelLevel, currentLocation);
        };
    }

    public static Vessel reconstruct(
            VesselType type,
            String vesselId,
            String name,
            int capacity,
            String route,
            double fuelLevel,
            String currentLocation) {
        return create(type, vesselId, name, capacity, route, fuelLevel, currentLocation);
    }
}
