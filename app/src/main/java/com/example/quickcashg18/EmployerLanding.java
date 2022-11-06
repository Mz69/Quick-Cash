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

public class EmployerLanding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_landing);
        Button signOutButton = findViewById(R.id.logout2);
        signOutButton.setOnClickListener(this::onClicklogout);
        Button roleSwitch = findViewById(R.id.role);
        roleSwitch.setOnClickListener(this::onClickRole);

        Button profile = findViewById(R.id.profileEmployer);
        profile.setOnClickListener(this::onClickProfile);

        Button PostJob = findViewById(R.id.post_job);
        PostJob.setOnClickListener(this::onClickPostJob);
    }

    private void commenceSignIn() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(EmployerLanding.this, SignInActivity.class));
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
                            startActivity(new Intent(EmployerLanding.this, SignInActivity.class));
                            finish();
                        }
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

}