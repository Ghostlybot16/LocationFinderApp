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

    }
}