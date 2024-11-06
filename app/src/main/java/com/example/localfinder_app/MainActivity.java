package com.example.localfinder_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.example.localfinder_app.DatabaseFunctions.Location;
import com.example.localfinder_app.DatabaseFunctions.LocationDatabaseHelper;


public class MainActivity extends AppCompatActivity {

    // Instance of LocationDatabaseHelper
    private LocationDatabaseHelper locationDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationDBHelper = new LocationDatabaseHelper();

        // Calling the setup method to initialize data in Firebase DB
        // Should be commented out after running it the first time
        // setupInitialDB();

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

        locationDBHelper.generateIDAndAddLocation("Sample Address3", 43.7, -79.2, new LocationDatabaseHelper.AddCallback() {
            @Override
            public void onAddSuccess() {
                Log.d("AddLocationSuccess", "Location added successfully.");
            }

            @Override
            public void onAddFailure() {
                Log.e("AddLocationError", "Failed to add location");
            }
        });

    }
}