package com.example.quickcashg18;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Handles the app flow on launch and on signout.
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase state
        FirebaseCommon.initDatabaseConstants();

        initDB();

        // Sign-in the user, if necessary.
        commenceSignIn();
    }

    private void initDB() {
        firebaseDB = FirebaseDatabase.getInstance();
    }

    private void commenceSignIn() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
        } else {
            // Get the user's current location and save it to the database.
            // Then, head to the next activity.
            ActivityResultLauncher<Void> getLocation = registerForActivityResult(new LocationResultContract(),
                    result -> {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        firebaseDB.getReference(FirebaseCommon.USER)
                                .child(user.getUid())
                                .child(MapsActivity.CURRENT_LOCATION)
                                .setValue(result);
                        startActivity(new Intent(getApplicationContext(), EmployeeLanding.class));
                    });
            getLocation.launch(null);
        }
    }

}