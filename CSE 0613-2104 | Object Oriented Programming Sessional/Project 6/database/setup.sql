CREATE DATABASE IF NOT EXISTS barishal_launch_ghat
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE barishal_launch_ghat;

CREATE TABLE IF NOT EXISTS vessels (
    vessel_id VARCHAR(30) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    route VARCHAR(150) NOT NULL,
    vessel_type VARCHAR(20) NOT NULL,
    fuel_level DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    current_location VARCHAR(150) NOT NULL,
    CONSTRAINT chk_vessel_capacity CHECK (capacity > 0),
    CONSTRAINT chk_vessel_type CHECK (vessel_type IN ('LAUNCH', 'SPEEDBOAT', 'TRAWLER')),
    CONSTRAINT chk_fuel_level CHECK (fuel_level >= 0)
);
