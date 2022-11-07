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

public class EmployeeLanding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_landing);
        Button signOutButton = findViewById(R.id.logout2);
        signOutButton.setOnClickListener(this::onClickLogout);
        Button roleSwitch = findViewById(R.id.role);
        roleSwitch.setOnClickListener(this::onClickRole);
        Button profile = findViewById(R.id.profileEmployee);
        profile.setOnClickListener(this::onClickProfile);
        Button findjob_button = findViewById(R.id.post_job);
        findjob_button.setOnClickListener(this::onClickFindJob);
    }

    public void onClickLogout(View view) {
        /** Sign-out implementation adapted from FirebaseUI Auth guide.
         Source: Source: https://firebaseopensource.com/projects/firebase/firebaseui-android/auth/readme/
         */
        if (view.getId() == R.id.logout2) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> {
                        // User is signed out. Start the sign-in process again.
                        startActivity(new Intent(EmployeeLanding.this, SignInActivity.class));
                        finish();
                    });

        }
    }
    public void onClickRole(View view) {
           Intent roleSwitch= (new Intent(EmployeeLanding.this, EmployerLanding.class));
           startActivity(roleSwitch);
    }

    public void onClickProfile(View view) {
        Intent goProfile = new Intent(EmployeeLanding.this, EmployeeProfile.class);
        startActivity(goProfile);
    }
    public void onClickFindJob(View view){
        startActivity(new Intent(EmployeeLanding.this, job_search.class));
    }

}

