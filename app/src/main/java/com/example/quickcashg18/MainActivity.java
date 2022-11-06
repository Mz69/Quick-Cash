package com.example.quickcashg18;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.quickcashg18.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Handles the app flow on launch and on signout.
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private FirebaseDatabase firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase state
        FirebaseConstants.initDatabaseConstants();

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
                    new ActivityResultCallback<Location>() {
                        @Override
                        public void onActivityResult(Location result) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            firebaseDB.getReference(FirebaseConstants.USER)
                                    .child(user.getUid())
                                    .child(MapsActivity.CURRENT_LOCATION)
                                    .setValue(result);
                            startActivity(new Intent(getApplicationContext(), EmployeeLanding.class));
                        }
                    });
            getLocation.launch(null);
        }
    }

}