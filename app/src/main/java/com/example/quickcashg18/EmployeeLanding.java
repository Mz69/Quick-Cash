package com.example.quickcashg18;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class EmployeeLanding extends AppCompatActivity {

    // initializing an arraylist to store the users notifications
    private ArrayList<String> notifications = new ArrayList<String>();

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
        // button to view user notifications
        Button notificationButton = findViewById(R.id.Notifications);
        notificationButton.setOnClickListener(this::onClickNotifications);
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

    public void addNotification(String message) {
        // adding the sent message to the users list of notifications
        notifications.add(message);
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

    public void onClickNotifications(View view){
        // displaying the users notifications
        for (int i=0;i<notifications.size();i++) {
            Toast.makeText(getApplicationContext(),notifications.get(i),Toast.LENGTH_LONG);
        }

    }

}

