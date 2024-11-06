package com.example.localfinder_app.DatabaseFunctions;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LocationDatabaseHelper {

    // Helper method to get the reference tot the "Locations" table in FirebaseDB
    // This helper method is created because this line of code is being referred to many times
    private DatabaseReference locationsDBRef() {
        return FirebaseDatabase.getInstance().getReference("Locations");
    }


    /** The commented out code below was initially created to test to see if the FirebaseDB connection worked.
     *  It is no longer being used but this was the first setup for the database to test.

//    // Method to set up initial location data in FirebaseDB
//    private void setupInitialDB() {
//
//        // Instance of the database
//        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
//
//        // Adding sample entry
//        locationsDBRef().child("1").setValue(new Location("123 Main St, Toronto, ON", 43.65107, -79.347015))
//                .addOnCompleteListener(task -> {
//
//                    // Check to see if operation was a success
//                    if (task.isSuccessful()) {
//
//                        // Log success message to Logcat
//                        Log.d("FirebaseSetup", "Sample location 1 added successfully");
//                    } else {
//
//                        // Log error message to Logcat
//                        Log.e("FirebaseSetup", "Failed to add location 1", task.getException());
//                    }
//                });
//
//        // Adding another sample entry
//        locationsDBRef().child("2").setValue(new Location("456 King St, Toronto, ON", 43.6555, -79.3806))
//                .addOnCompleteListener(task -> {
//
//                    // Check to see if operation was a success
//                    if (task.isSuccessful()) {
//
//                        // Log success message to Logcat
//                        Log.d("FirebaseSetup", "Sample location 2 added successfully");
//                    } else {
//
//                        // Log error message to Logcat
//                        Log.e("FirebaseSetup", "Failed to add location 2", task.getException());
//                    }
//                });
//    } */

    // Method to add a new location to FirebaseDB
    public void addNewLocationInDB(String id, String address, double latitude, double longitude) {

        // Create a new location object with the user-input details
        Location newLocationDBEntry = new Location(address, latitude, longitude);

        // Refer to the "Locations" table and add the new location under the specified ID
        locationsDBRef().child(id).setValue(newLocationDBEntry)
                .addOnCompleteListener(task -> { // Listener to handle the result of the operation

                    // Check to see if operation was successful
                    if(task.isSuccessful()) {

                        // Log message indicating success
                        Log.d("AddLocationDBSuccess", "New location added successfully. ");
                    } else {

                        // Log message indicating fail
                        Log.e("AddLocationDBError", "Failed to add new location.", task.getException());
                    }
                });
    }

    // CRUD (Create)-----------------------------------------------------------------------------------------------------
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
                addNewLocationInDB(newID, address, latitude, longitude);
            } else {

                // Log an error message if retrieving existing entries from FirebaseDB failed
                Log.e("GenerateNextIDError", "Failed to retrieve existing IDs", task.getException());
            }
        });
    }


    // CRUD (READ)-----------------------------------------------------------------------------------------------------
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

    // CRUD (UPDATE)-----------------------------------------------------------------------------------------------------
    // Method to update data
    public void updateLocationByAddress(String address, String updatedAddress, double updatedLatitude, double updatedLongitude, UpdateCallback callback) {

        // Query for the location by address in the DB
        queryLocationByAddress(address, new AddressQueryCallback() {
            @Override
            public void onAddressQuerySuccess(String id, Location location) {

                // Update the data fields
                locationsDBRef().child("address").setValue(updatedAddress);
                locationsDBRef().child("latitudeValue").setValue(updatedLatitude);
                locationsDBRef().child("longitudeValue").setValue(updatedLongitude)
                        .addOnCompleteListener(task -> {

                            if(task.isSuccessful()) {
                                Log.d("UpdateLocation_Success", "Location updated successfully");
                                callback.onUpdateSuccess();
                            } else {
                                Log.e("UpdateLocation_Error", "Failed to update location", task.getException());
                                callback.onUpdateFailure();
                            }
                        });
            }

            @Override
            public void onAddressQueryFailure() {
                Log.d("UpdateLocationError", "Location not found for address: " + address);
                callback.onUpdateFailure();
            }
        });


    }

    // Method to delete a location from Firebase based on its ID
    public void deleteLocationByIDFirebase(String id, DeleteCallback callback) {
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

    // CRUD (DELETE)-----------------------------------------------------------------------------------------------------
    // Method to delete a location from Firebase by searching for its address
    public void deleteLocationByAddress(String address, DeleteCallback callback) {

        // Query Firebase for the location by address
        queryLocationByAddress(address, new AddressQueryCallback() {
            @Override
            public void onAddressQuerySuccess(String id, Location location) {

                // Delete the location by calling deleteLocationByID method with the found ID
                deleteLocationByIDFirebase(id, new DeleteCallback() {
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

    /*  AddressQueryCallback interface defines a way for the queryLocationByAddress method to communicate
     *   results back once it finishes retrieving data from Firebase. */
    public interface AddressQueryCallback {

        // Called when location is found in Firebase with the given address
        void onAddressQuerySuccess(String id, Location location);

        // Called when no location is found in Firebase
        void onAddressQueryFailure();
    }

    // Callback interface to handle deletion result
    public interface DeleteCallback {
        void onDeleteSuccess(); // Called if deletion is successful
        void onDeleteFailure(); // Called if deletion fails
    }

    // Callback interface to handle updates
    public interface UpdateCallback {
        void onUpdateSuccess();
        void onUpdateFailure();
    }


}
