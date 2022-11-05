package com.example.quickcashg18;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class employee_landing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_landing);
        Button signOutButton = findViewById(R.id.logout2);
        signOutButton.setOnClickListener(this::onClicklogout);
        Button roleSwitch = findViewById(R.id.role);
        roleSwitch.setOnClickListener(this::onClickRole);
        Button profile = findViewById(R.id.profileEmployee);
        profile.setOnClickListener(this::onClickProfile);
    }

    private void commenceSignIn() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(employee_landing.this, SignInActivity.class));
        }
    }
    public void onClicklogout(View view) {
        /** Sign-out implementation adapted from FirebaseUI Auth guide.
         Source: Source: https://firebaseopensource.com/projects/firebase/firebaseui-android/auth/readme/
         */
        if (view.getId() == R.id.logout2) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // User is signed out. Start the sign-in process again.
                            startActivity(new Intent(employee_landing.this, SignInActivity.class));
                            finish();
                        }
                    });

        }
    }
    public void onClickRole(View view) {
           Intent roleSwitch= (new Intent(employee_landing.this, employer_landing.class));
           startActivity(roleSwitch);
    }

    public void onClickProfile(View view) {
        Intent goProfile = new Intent(employee_landing.this, EmployeeProfile.class);
        startActivity(goProfile);
    }

}

