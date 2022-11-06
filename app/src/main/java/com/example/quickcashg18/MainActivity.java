package com.example.quickcashg18;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

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

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase state
        FirebaseConstants.initDatabaseConstants();

        // Sign-in the user, if necessary.
        commenceSignIn();
    }

    private void commenceSignIn() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
        } else {
            //startActivity(new Intent(this, employee_landing.class));
            ActivityResultLauncher<String> getLocation = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        Location location = uri.;
                    }
                })

            startActivity(new Intent(this, MapsActivity.class));
            startActivity(new Intent(this, employee_landing.class));
        }
    }

}