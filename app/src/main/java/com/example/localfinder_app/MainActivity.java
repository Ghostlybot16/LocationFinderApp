package com.example.localfinder_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log; // Allows to use the Log class to send log messages to Logcat
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase; // Importing Firebase DB

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Calling the setup method to initialize data in Firebase DB
        // Should be commented out after running it the first time
        // setupInitialDB();
    }

    // Method to set up initial location data in FirebaseDB
    private void setupInitialDB() {

        // Instance of the database
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();

        // Reference to the "locations" node in Firebase DB
        DatabaseReference locationsRef = firebaseDB.getReference("Locations");

        // Adding sample entry
        locationsRef.child("1").setValue(new Location("123 Main St, Toronto, ON", 43.65107, -79.347015))
                .addOnCompleteListener(task -> {

                    // Check to see if operation was a success
                    if (task.isSuccessful()) {

                        // Log success message to Logcat
                        Log.d("FirebaseSetup", "Sample location 1 added successfully");
                    } else {

                        // Log error message to Logcat
                        Log.e("FirebaseSetup", "Failed to add location 1", task.getException());
                    }
                });

        // Adding another sample entry
        locationsRef.child("2").setValue(new Location("456 King St, Toronto, ON", 43.6555, -79.3806))
                .addOnCompleteListener(task -> {

                    // Check to see if operation was a success
                    if (task.isSuccessful()) {

                        // Log success message to Logcat
                        Log.d("FirebaseSetup", "Sample location 2 added successfully");
                    } else {

                        // Log error message to Logcat
                        Log.e("FirebaseSetup", "Failed to add location 2", task.getException());
                    }
                });
    }

    // Location class to represent each location entry in Firebase DB
    public static class Location {
        public String address; // Stores the address of the location
        public double latitudeValue; // Stores the latitude coordinate
        public double longitudeValue; // Stores the longitude coordinate

        // Default constructor
        @SuppressWarnings("unused") // Suppresses "unused" warning
        public Location() {
        }

        public Location(String address, double latitude, double longitude) {
            this.address = address;
            this.latitudeValue = latitude;
            this.longitudeValue = longitude;
        }
    }
}