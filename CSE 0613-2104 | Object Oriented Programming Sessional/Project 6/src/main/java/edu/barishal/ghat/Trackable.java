package edu.barishal.ghat;

public interface Trackable {
    String getCurrentLocation();

    void setCurrentLocation(String currentLocation);

    default String getTrackingStatus() {
        return "Last reported location: " + getCurrentLocation();
    }
}
