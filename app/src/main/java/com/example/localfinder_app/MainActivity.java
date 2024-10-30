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

        // Initialize Firebase Realtime Database
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = firebaseDB.getReference("testNode");

        // Write a message to the DB
        dbRef.setValue("Firebase setup successful!") // The output to the Firebase console
                .addOnCompleteListener( task -> {

                    // Output to Logcat terminal
                    // Tag is the tag name that appears in Logcat
                    // Msg is the message attached to the Tag name.
                    if (task.isSuccessful()) {
                        Log.d("FirebaseTest", "Data written successfully"); // Debug message
                    } else {
                        Log.e("FirebaseTest", "Failed to write data", task.getException()); // Error message
                    }
                });
    }
}