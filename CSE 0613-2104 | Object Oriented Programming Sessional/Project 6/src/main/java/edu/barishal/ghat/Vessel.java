package edu.barishal.ghat;

public abstract class Vessel {
    private final String vesselId;
    private final String name;
    private final int capacity;
    private final String route;
    private double fuelLevel;
    private String currentLocation;

    protected Vessel(
            String vesselId,
            String name,
            int capacity,
            String route,
            double fuelLevel,
            String currentLocation) {
        this.vesselId = requireText(vesselId, "Vessel ID");
        this.name = requireText(name, "Name");
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        if (!Double.isFinite(fuelLevel) || fuelLevel < 0) {
            throw new IllegalArgumentException(
                    "Fuel level must be a finite, non-negative number.");
        }
        this.capacity = capacity;
        this.route = requireText(route, "Route");
        this.fuelLevel = fuelLevel;
        this.currentLocation = requireText(currentLocation, "Current location");
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    public String getVesselId() {
        return vesselId;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getRoute() {
        return route;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(double fuelLevel) {
        if (!Double.isFinite(fuelLevel) || fuelLevel < 0) {
            throw new IllegalArgumentException(
                    "Fuel level must be a finite, non-negative number.");
        }
        this.fuelLevel = fuelLevel;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = requireText(currentLocation, "Current location");
    }

    public abstract VesselType getVesselType();

    public String getSummary() {
        return getVesselType().getDisplayName() + " \"" + name + "\""
                + " (ID: " + vesselId + "), capacity: " + capacity
                + ", route: " + route + ", fuel: " + String.format("%.2f L", fuelLevel);
    }

    @Override
    public String toString() {
        return vesselId + " - " + name + " (" + getVesselType().getDisplayName() + ")";
    }
}
