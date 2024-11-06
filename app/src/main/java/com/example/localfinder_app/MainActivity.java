package com.example.localfinder_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log; // Allows to use the Log class to send log messages to Logcat

import com.google.firebase.database.FirebaseDatabase; // Importing Firebase DB

import DatabaseFunctions.LocationDatabaseHelper;


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

        // addNewLocation("3", "321 Test Avenue, Toronto, ON", 43.1234, -43.3213);

        // generateIDAndAddLocation("434 Avenue Drive, Toronto, ON", 43.5234, -79.2348);
        locationDBHelper.deleteLocationByAddress(userAddressInput);

    }
}