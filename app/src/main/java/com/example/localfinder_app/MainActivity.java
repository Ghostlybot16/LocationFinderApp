package com.example.localfinder_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localfinder_app.DatabaseFunctions.Location;
import com.example.localfinder_app.DatabaseFunctions.LocationDatabaseHelper;


public class MainActivity extends AppCompatActivity {

    // Instance of LocationDatabaseHelper
    private LocationDatabaseHelper locationDBHelper;
    private String originalAddress; // Holds the address retrieved from the DB

    // Declaring variables for UI components
    private EditText addressInputField;
    private Button queryButton, addLocationButton;
    private LinearLayout queryResultDisplayArea;
    private TextView displayAddress, displayLongitude, displayLatitude;
    private ImageButton editButton, deleteButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationDBHelper = new LocationDatabaseHelper();
        addressInputField = findViewById(R.id.addressInputField);
        queryButton = findViewById(R.id.queryButton);
        addLocationButton = findViewById(R.id.addLocationButton);
        queryResultDisplayArea = findViewById(R.id.queryResultDisplayArea);
        displayAddress = findViewById(R.id.displayAddress);
        displayLongitude = findViewById(R.id.displayLongitude);
        displayLatitude = findViewById(R.id.displayLatitude);
        editButton = findViewById(R.id.edit);
        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);

        // Instance of Location Uploader
        // SampleLocationUploader dataUploader = new SampleLocationUploader();
        // dataUploader.addSampleLocations(); // Upload locations into FirebaseDB

        // Query Button
        queryButton.setOnClickListener(view -> {
            String address = addressInputField.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter an address to query", Toast.LENGTH_SHORT).show();
                return;
            }

            locationDBHelper.queryLocationByAddress(address, new LocationDatabaseHelper.AddressQueryCallback() {
                @Override
                public void onAddressQuerySuccess(String id, Location location) {
                    originalAddress = location.address; // Set originalAddress to the retrieved address
                    displayAddress.setText(location.address);
                    displayLongitude.setText(String.valueOf(location.longitudeValue));
                    displayLatitude.setText(String.valueOf(location.latitudeValue));
                    queryResultDisplayArea.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAddressQueryFailure() {
                    Toast.makeText(MainActivity.this, "Address not found", Toast.LENGTH_SHORT).show();
                    queryResultDisplayArea.setVisibility(View.GONE);
                }
            });
        });


        // Add new location button
        addLocationButton.setOnClickListener(view -> {

            // Example data for testing
            String newAddress = "123 New Street, Toronto, ON";
            double latitude = 43.12345;
            double longitude = -79.12345;

            locationDBHelper.generateIDAndAddLocation(newAddress, latitude, longitude, new LocationDatabaseHelper.AddCallback() {
                @Override
                public void onAddSuccess() {
                    Toast.makeText(MainActivity.this, "Location added successfully.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAddFailure() {
                    Toast.makeText(MainActivity.this, "Failed to add location.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Edit button (Pencil Icon)
        editButton.setOnClickListener(view -> {

            // Make TextViews editable
            displayAddress.setEnabled(true);
            displayLongitude.setEnabled(true);
            displayLatitude.setEnabled(true);
            saveButton.setVisibility(View.VISIBLE);
        });

        saveButton.setOnClickListener(view -> {
            // Retrieve values directly as strings
            String updatedAddress = displayAddress.getText().toString();
            double updatedLongitude = Double.parseDouble(displayLongitude.getText().toString());
            double updatedLatitude = Double.parseDouble(displayLatitude.getText().toString());

            // Pass the original address as the first argument
            locationDBHelper.updateLocationByAddress(originalAddress, updatedAddress, updatedLatitude, updatedLongitude, new LocationDatabaseHelper.UpdateCallback() {
                @Override
                public void onUpdateSuccess() {
                    Toast.makeText(MainActivity.this, "Location updated successfully.", Toast.LENGTH_SHORT).show();
                    saveButton.setVisibility(View.GONE);
                }

                @Override
                public void onUpdateFailure() {
                    Toast.makeText(MainActivity.this, "Failed to update location.", Toast.LENGTH_SHORT).show();
                }
            });
        });



        deleteButton.setOnClickListener(view -> {
            String addressToDelete = displayAddress.getText().toString();

            locationDBHelper.deleteLocationByAddress(addressToDelete, new LocationDatabaseHelper.DeleteCallback() {
                @Override
                public void onDeleteSuccess() {
                    Toast.makeText(MainActivity.this, "Location deleted successfully!", Toast.LENGTH_SHORT).show();
                    queryResultDisplayArea.setVisibility(View.GONE);
                }

                @Override
                public void onDeleteFailure() {
                    Toast.makeText(MainActivity.this, "Failed to delete location.", Toast.LENGTH_SHORT).show();
                }
            });
        });


        // Calling the setup method to initialize data in Firebase DB
        // Should be commented out after running it the first time
        // locationDBHelper.setupInitialDB();

        // Placeholder value for user-input address input. This value will come from the UI later on
        String userAddressInput = "123 Main St, Toronto, ON";


        // ----------------------------------------------------------------------------------------------------------

        // Test Add Location function after creating the new LocationDBHelper file
        // This works
        // locationDBHelper.generateIDAndAddLocation("111 Testing Value, Toronto, ON", 43.65432, -77.12345);

        // Test Read (Query) location function after creating the new LocationDBHelper file
        // This works
//        locationDBHelper.queryLocationByAddress("111 Testing Value, Toronto, ON", new LocationDatabaseHelper.AddressQueryCallback() {
//            @Override
//            public void onAddressQuerySuccess(String id, Location location) {
//                Log.d("ReadTest_Success", "Location found: " + location.address + ", Lat: " + location.latitudeValue + ", Lon: " + location.longitudeValue);
//            }
//
//            @Override
//            public void onAddressQueryFailure() {
//                Log.d("ReadTest_Error", "Location not found.");
//            }
//        });



        // Test Delete location function after creating the new LocationDBHelper file
        // This works
//        locationDBHelper.deleteLocationByAddress("111 Testing Value, Toronto, ON", new LocationDatabaseHelper.DeleteCallback() {
//            @Override
//            public void onDeleteSuccess() {
//                Log.d("DeleteTest_Success", "Location deleted successfully");
//            }
//
//            @Override
//            public void onDeleteFailure() {
//                Log.d("DeleteTest_Error", "Failed to delete location.");
//            }
//        });

//        // Test Values
        // This works
//        String testAddress = "434 Avenue Drive, Toronto, ON";
//        String newAddress = "444 Updated Street, Toronto, ON"; // New address to update
//        double newLatitude = 43.7001;
//        double newLongitude = -79.7001;
//
//        // Test Update By Address
//        locationDBHelper.updateLocationByAddress(testAddress, newAddress, newLatitude, newLongitude, new LocationDatabaseHelper.UpdateCallback() {
//            @Override
//            public void onUpdateSuccess() {
//                Log.d("UpdateTest_Success", "Location updates successfully.");
//            }
//
//            @Override
//            public void onUpdateFailure() {
//                Log.d("UpdateTest_Fail", "Failed to update location.");
//            }
//        });

        // Test to attempt to add a location with invalid Longitude and Latitude
        // This test works as intended.
//        String invalidAddress = "Invalid Address, Toronto, ON";
//        double invalidLatitude = 95.7823; // Invalid latitude
//        double invalidLongitude  = -185.2345; // Invalid longitude
//        locationDBHelper.generateIDAndAddLocation(invalidAddress, invalidLatitude, invalidLongitude);

        // Test to attempt to update a location with invalid longitude and latitude
        // This test runs as intended, error message received for the invalid coords
//        String existingAddress = "433 Avenue Drive, Toronto, ON";
//        String newUpdatedAddress = "Updated Address, Toronto, ON";
//        double newInvalidLatitude = 100;
//        double newInvalidLongitude = 200;
//        locationDBHelper.updateLocationByAddress(existingAddress, newUpdatedAddress, newInvalidLongitude, newInvalidLatitude, new LocationDatabaseHelper.UpdateCallback() {
//            @Override
//            public void onUpdateSuccess() {
//                Log.d("TestUpdateSuccess", "Location update successful.");
//            }
//
//            @Override
//            public void onUpdateFailure() {
//                Log.d("TestUpdateFail", "Location update failed due to invalid longitude/latitude value");
//            }
//        });

//        locationDBHelper.generateIDAndAddLocation("Sample Address3", 43.7, -79.2, new LocationDatabaseHelper.AddCallback() {
//            @Override
//            public void onAddSuccess() {
//                Log.d("AddLocationSuccess", "Location added successfully.");
//            }
//
//            @Override
//            public void onAddFailure() {
//                Log.e("AddLocationError", "Failed to add location");
//            }
//        });

    }
}