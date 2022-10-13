package com.example.quickcashg18;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sign-in the user, if necessary.
        commenceSignIn();

        setContentView(R.layout.activity_main);

        Button signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);
    }

    private void commenceSignIn() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
        }
    }

    public void onClick(View view) {
        /** Sign-out implementation adapted from FirebaseUI Auth guide.
            Source: Source: https://firebaseopensource.com/projects/firebase/firebaseui-android/auth/readme/
         */
        if (view.getId() == R.id.signOutButton) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // User is signed out. Start the sign-in process again.
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
                            finish();
                        }
                    });

        }
    }

}