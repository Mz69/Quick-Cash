package com.example.quickcashg18;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeLanding extends AppCompatActivity {

    // initializing an arraylist to store the users notifications
    private ArrayList<String> notifications = new ArrayList<>();

    private DatabaseReference userRef;
    private FirebaseUser user;

    private Button signOutButton;
    private Button roleSwitch;
    private Button profile;
    private Button findJobButton;
    private Button notificationButton;
    private Button pastJobsButton;
    private RatingBar ratingBar;
    private TextView totalIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_landing);

        initDatabase();
        initViews();
        initListeners();
    }

    protected void initDatabase() {
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = firebaseDB.getReference()
                .child(FirebaseConstants.USER)
                .child(user.getUid());
    }

    protected void initViews() {
        signOutButton = findViewById(R.id.logout2);
        roleSwitch = findViewById(R.id.role);
        profile = findViewById(R.id.profileEmployee);
        findJobButton = findViewById(R.id.post_job);
        notificationButton = findViewById(R.id.Notifications);
        pastJobsButton = findViewById(R.id.past_jobs_employer);

        ratingBar = findViewById(R.id.employeeLandingRatingBar);
        totalIncome = findViewById(R.id.employeeLandingIncomeDescriptor);

        FirebaseConstants.calculateRatingOfEmployee(user.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Float rating = snapshot.child(FirebaseConstants.EMPLOYEE_RATING)
                        .getValue(Float.class);
                Double income = snapshot.child(FirebaseConstants.USER_INCOME)
                        .getValue(Double.class);
                if (rating != null) {
                    ratingBar.setRating(rating);
                    ratingBar.setIsIndicator(true);
                }
                if (income != null) {
                    totalIncome.setText(String.valueOf(income));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EmployeeLanding", error.getMessage());
            }
        });
    }

    protected void initListeners() {
        signOutButton.setOnClickListener(this::onClickLogout);
        roleSwitch.setOnClickListener(this::onClickRole);
        profile.setOnClickListener(this::onClickProfile);
        findJobButton.setOnClickListener(this::onClickFindJob);
        // button to view user notifications
        notificationButton.setOnClickListener(this::onClickNotifications);
        pastJobsButton.setOnClickListener(this::onClickPastJobs);
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
           Intent switchToEmployerLanding = new Intent(EmployeeLanding.this, EmployerLanding.class);
           startActivity(switchToEmployerLanding);
    }

    public void onClickProfile(View view) {
        Intent goProfile = new Intent(EmployeeLanding.this, EmployeeProfile.class);
        startActivity(goProfile);
    }
    public void onClickFindJob(View view){
        startActivity(new Intent(EmployeeLanding.this, JobSearch.class));
    }

    public void onClickNotifications(View view){
        // displaying the users notifications
        for (int i=0;i<notifications.size();i++) {
            Toast.makeText(getApplicationContext(),notifications.get(i),Toast.LENGTH_LONG);
        }

    }

    public void onClickPastJobs(View view) {
        startActivity(new Intent(EmployeeLanding.this, EmployeePastJobs.class));
    }

}

