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

    // Helper method to get the reference tot the "Locations" table in FirebaseDB
    // This helper method is created because this line of code is being referred to many times
    private DatabaseReference locationsDBRef() {
        return FirebaseDatabase.getInstance().getReference("Locations");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Calling the setup method to initialize data in Firebase DB
        // Should be commented out after running it the first time
        // setupInitialDB();

        // Placeholder value for user-input address input. This value will come from the UI later on
        String userAddressInput = "123 Main St, Toronto, ON";

        // addNewLocation("3", "321 Test Avenue, Toronto, ON", 43.1234, -43.3213);

        // generateIDAndAddLocation("434 Avenue Drive, Toronto, ON", 43.5234, -79.2348);
        deleteLocationByAddress(userAddressInput);

    }


    // Method to set up initial location data in FirebaseDB
    private void setupInitialDB() {

        // Instance of the database
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();

        // Adding sample entry
        locationsDBRef().child("1").setValue(new Location("123 Main St, Toronto, ON", 43.65107, -79.347015))
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
        locationsDBRef().child("2").setValue(new Location("456 King St, Toronto, ON", 43.6555, -79.3806))
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

    // NEW Method to delete a location from Firebase by searching for its address
    public void deleteLocationByAddress(String address) {

        // Query Firebase for the location by address
        queryLocationByAddress(address, new AddressQueryCallback() {
            @Override
            public void onAddressQuerySuccess(String id, Location location) {

                // Delete the location by calling deleteLocationByID method with the found ID
                deleteLocationByID(id, new DeleteCallback() {
                    @Override
                    public void onDeleteSuccess() {
                        Log.d("DeleteLocationSuccess", "Location deleted successfully.");
                    }

                    @Override
                    public void onDeleteFailure() {
                        Log.e("DeleteLocationFail", "Failed to delete location.");
                    }
                });

            }

            @Override
            public void onAddressQueryFailure() {
                Log.d("DeleteLocationError", "Location not found for address: " + address);
            }
        });
    }

    // Method to query FirebaseDB by user-input address and result results from DB through a callback
    public void queryLocationByAddress(String userAddressInput, AddressQueryCallback callback) {

        // Reference to the "Locations" node in FirebaseDB
        // DatabaseReference locationsDBRef = FirebaseDatabase.getInstance().getReference("Locations");

        // Create a query that searches for the given address
        Query query = locationsDBRef().orderByChild("address").equalTo(userAddressInput);

        // Listener to retrieve data from FirebaseDB
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Check to see if any data was returned
                if (dataSnapshot.exists()) {

                    // Loop through the children snapshot
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String id = snapshot.getKey();
                        Location locationQueryResult = snapshot.getValue(Location.class); // Retrieve location details

                        if(locationQueryResult != null) {
                            callback.onAddressQuerySuccess(id, locationQueryResult); // Pass the found ID and location to the callback
                            return;
                        }
                    }
                } else {
                    // callback.queryResult(null);
                    callback.onAddressQueryFailure();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Log an error if query fails
                Log.e("QueryResult", "Database query failed", databaseError.toException());

                // callback.queryResult(null);
                callback.onAddressQueryFailure();
            }
        });
    }

    // Method to generate the next available ID value based on the ID value that already exists in the FirebaseDB
    public void generateIDAndAddLocation(String address, double latitude, double longitude) {

        // Retrieve all existing entries within "Locations" table in Firebase to find the current highest ID value
        locationsDBRef().get().addOnCompleteListener(task -> {

            // Check to see if retrieving data from FirebaseDB was successful AND if it returns a non-null value
            if (task.isSuccessful() && task.getResult() != null) {

                DataSnapshot locationsSnapshot = task.getResult(); // Snapshot of all children under "Location" table in FirebaseDB
                int highestExistingID = 0;

                // Convert DataSnapshot children to an array to allow index-based access
                DataSnapshot[] locationsDataEntries = new DataSnapshot[(int) locationsSnapshot.getChildrenCount()];
                int arrayIndex = 0;
                for (DataSnapshot childSnapshot : locationsSnapshot.getChildren()) {
                    locationsDataEntries[arrayIndex++] = childSnapshot;
                }

                // Index-based for loop to iterate over the array of snapshots
                for(int i = 0; i < locationsDataEntries.length; i++) {
                    try{
                        int id = Integer.parseInt(locationsDataEntries[i].getKey());
                        if (id > highestExistingID) {
                            highestExistingID = id;
                        }
                    } catch (NumberFormatException e) {
                        Log.e("GenerateNextID", "Invalid ID format in Firebase database", e);
                    }
                }

                // Generate the next ID by incrementing the highest found ID
                int nextID = highestExistingID + 1;
                String newID = String.valueOf(nextID); // Convert nextID to String for Firebase

                // Call addNewLocation method with the generated ID
                addNewLocation(newID, address, latitude, longitude);
            } else {

                // Log an error message if retrieving existing entries from FirebaseDB failed
                Log.e("GenerateNextIDError", "Failed to retrieve existing IDs", task.getException());
            }
        });
    }

    // Method to add a new location to FirebaseDB
    public void addNewLocation(String id, String address, double latitude, double longitude) {

        // Reference to the "Locations" table in FirebaseDB
        // DatabaseReference locationsDBRef = FirebaseDatabase.getInstance().getReference("Locations");

        // Create a new location object with the user-input details
        Location newLocationDBEntry = new Location(address, latitude, longitude);

        // Add the new location under the specified ID
        locationsDBRef().child(id).setValue(newLocationDBEntry)
                .addOnCompleteListener(task -> { // Listener to handle the result of the operation

                    // Check to see if operation was successful
                    if(task.isSuccessful()) {

                        // Log message indicating success
                        Log.d("AddLocationSuccess", "New location added successfully. ");
                    } else {

                        // Log message indicating fail
                        Log.e("AddLocationError", "Failed to add new location.", task.getException());
                    }
                });
    }

    // NEW Method to delete a location from Firebase based on its ID
    public void deleteLocationByID(String id, DeleteCallback callback) {
        locationsDBRef().child(id).removeValue()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d("DeleteLocationSuccess", "Location with ID " + id + " deleted successfully.");
                        callback.onDeleteSuccess();
                    } else {
                        Log.e("DeleteLocationError", "Failed to delete location with ID " + id, task.getException());
                        callback.onDeleteFailure();
                    }
                });
    }

    public interface AddressQueryCallback {
        void onAddressQuerySuccess(String id, Location location);
        void onAddressQueryFailure();
    }

    // Callback interface to handle deletion result
    public interface DeleteCallback {
        void onDeleteSuccess(); // Called if deletion is successful
        void onDeleteFailure(); // Called if deletion fails
    }


    // Location class to represent each location entry in Firebase DB
    public static class Location {
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
}