package edu.barishal.ghat;

public enum VesselType {
    LAUNCH("Launch"),
    SPEEDBOAT("Speedboat"),
    TRAWLER("Trawler");

    private final String displayName;

    VesselType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static VesselType fromDatabaseValue(String value) {
        return VesselType.valueOf(value.trim().toUpperCase());
    }

    @Override
    public String toString() {
        return displayName;
    }
}
