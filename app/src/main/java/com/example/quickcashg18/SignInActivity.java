package com.example.quickcashg18;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quickcashg18.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
This class is adapted from the FirebaseUI tutorial.
Source: https://firebaseopensource.com/projects/firebase/firebaseui-android/auth/readme/
 */

public class SignInActivity extends AppCompatActivity {

    // After a successful sign-in, the MainActivity is launched.
    private ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            (result) -> {
                startActivity(new Intent(this, MainActivity.class));
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Note that the sign-in launcher disables Smart Lock.
        // This is recommended, otherwise an error is thrown.
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .build();
        signInLauncher.launch(signInIntent);

    }
}
