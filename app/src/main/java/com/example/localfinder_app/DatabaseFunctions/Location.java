package com.example.localfinder_app.DatabaseFunctions;

// Location class to represent each location entry in Firebase DB
public class Location {

        public String address; // Stores the address of the location
        public double latitudeValue; // Stores the latitude coordinate
        public double longitudeValue; // Stores the longitude coordinate

        // Default constructor
        @SuppressWarnings("unused") // Suppresses "unused" warning
        public Location() {}

        public Location(String address, double latitude, double longitude) {
            this.address = address;
            this.latitudeValue = latitude;
            this.longitudeValue = longitude;
        }
}
