package com.example.quickcashg18;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;

public class EmployerLanding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_landing);
        Button signOutButton = findViewById(R.id.logout2);
        signOutButton.setOnClickListener(this::onClicklogout);
        Button roleSwitch = findViewById(R.id.role);
        roleSwitch.setOnClickListener(this::onClickRole);
        Button pastJob =findViewById(R.id.past_jobs_employer);
        pastJob.setOnClickListener(this::onClickPastJob);

        Button profile = findViewById(R.id.profileEmployer);
        profile.setOnClickListener(this::onClickProfile);

        Button postJob = findViewById(R.id.post_job);
        postJob.setOnClickListener(this::onClickPostJob);
    }

    public void onClicklogout(View view) {
        /** Sign-out implementation adapted from FirebaseUI Auth guide.
         Source: Source: https://firebaseopensource.com/projects/firebase/firebaseui-android/auth/readme/
         */
        if (view.getId() == R.id.logout2) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> {
                        // User is signed out. Start the sign-in process again.
                        startActivity(new Intent(EmployerLanding.this, SignInActivity.class));
                        finish();
                    });

        }
    }
    public void onClickRole(View view) {
        Intent roleSwitch= (new Intent(EmployerLanding.this, EmployeeLanding.class));
        startActivity(roleSwitch);
    }

    public void onClickProfile(View view) {
        Intent goProfile = new Intent(EmployerLanding.this, EmployerProfile.class);
        startActivity(goProfile);
    }

    public void onClickPostJob(View view) {
        Intent postJob= (new Intent(EmployerLanding.this, PostJob.class));
        startActivity(postJob);
    }
    public void onClickPastJob(View view) {
        Intent pastJob = new Intent(EmployerLanding.this, EmployerPastJobs.class);
        startActivity(pastJob);
    }

}