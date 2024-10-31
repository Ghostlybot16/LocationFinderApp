package com.example.localfinder_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log; // Allows to use the Log class to send log messages to Logcat
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase; // Importing Firebase DB
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Calling the setup method to initialize data in Firebase DB
        // Should be commented out after running it the first time
        // setupInitialDB();

        // Placeholder value for user-input address input. This value will come from the UI later on
        String userAddressInput = "123 Main St, Toronto, ON";

        // Call queryLocationByAddress method with the sample address and handle result using callback
        queryLocationByAddress(userAddressInput, new QueryResultCallback() {
            @Override

            // Method to handle the query results once they are retrieved from FirebaseDB
            // This method allows the queryLocationByAddress method to notify once the query is complete
            public void queryResult(Location locationReceivedFromFirebase) {

                // Check to see if location was found from FirebaseDB
                if (locationReceivedFromFirebase != null) {

                    // Log the latitude and longitude of the found location into Logcat
                    Log.d("QueryResult", "Latitude: " + locationReceivedFromFirebase.latitudeValue);
                    Log.d("QueryResult", "Longitude: " + locationReceivedFromFirebase.longitudeValue);

                } else {

                    // Log message when no matching address was found
                    Log.d("QueryErrorResult", "No matching address found.");
                }
            }
        });
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


    // Method to query FirebaseDB by user-input address and result results from DB through a callback
    public void queryLocationByAddress(String userAddressInput, QueryResultCallback callback) {

        // Reference to the "Locations" node in FirebaseDB
        DatabaseReference locationsDBRef = FirebaseDatabase.getInstance().getReference("Locations");

        // Create a query that searches for the given address
        Query query = locationsDBRef.orderByChild("address").equalTo(userAddressInput);

        // Listener to retrieve data from FirebaseDB
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Check to see if any data was returned
                if (dataSnapshot.exists()) {

                    // Array creation to hold snapshots of matching results
                    DataSnapshot[] locationSnapshotsArray = new DataSnapshot[(int) dataSnapshot.getChildrenCount()];
                    int snapshotIndex = 0;

                    // Convert dataSnapshot's children to an array
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        locationSnapshotsArray[snapshotIndex++] = snapshot;
                    }

                    // For loop to process each snapshot in the array
                    Location locationQueryResult = null;
                    for (int arrayIndex = 0; arrayIndex < locationSnapshotsArray.length; arrayIndex++) {
                        locationQueryResult = locationSnapshotsArray[arrayIndex].getValue(Location.class);

                        // Breaks after retrieving the first valid location
                        if (locationQueryResult != null)
                            break;
                    }

                    // Pass the found location to the callback
                    callback.queryResult(locationQueryResult);

                } else {
                    callback.queryResult(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Log an error if query fails
                Log.e("QueryResult", "Database query failed", databaseError.toException());

                callback.queryResult(null);
            }
        });
    }

    // Callback interface to handle query results
    public interface QueryResultCallback {
        void queryResult(Location locationReceivedFromFirebase);
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