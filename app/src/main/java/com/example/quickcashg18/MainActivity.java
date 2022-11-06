package com.example.quickcashg18;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import android.view.View;
import android.widget.Button;

import com.example.quickcashg18.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
            ActivityResultLauncher<Void> getLocation = registerForActivityResult(new LocationResultContract(),
                    new ActivityResultCallback<Location>() {
                        // When the MapsActivity passes back the user's current location,
                        // save it into their database section.
                        // Then, head to the next activity.
                        @Override
                        public void onActivityResult(Location result) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            firebaseDB.getReference(FirebaseConstants.USER)
                                    .child(user.getUid())
                                    .child(MapsActivity.CURRENT_LOCATION)
                                    .setValue(result);
                            startActivity(new Intent(getApplicationContext(), employee_landing.class));
                        }
                    });
            getLocation.launch(null);
        }
    }

}