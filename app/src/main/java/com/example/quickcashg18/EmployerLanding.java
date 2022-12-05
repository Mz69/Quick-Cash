package com.example.quickcashg18;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployerLanding extends AppCompatActivity {

    private FirebaseDatabase firebaseDB;
    private DatabaseReference userRef;
    private FirebaseUser user;

    private Button signOutButton;
    private Button roleSwitch;
    private Button pastJob;
    private Button profile;
    private Button postJob;
    private RatingBar ratingBar;
    private Button notificationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_landing);
        initDatabase();
        initViews();
        initListeners();
    }

    protected void initDatabase() {
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FirebaseCommon.FIREBASE_URL);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = firebaseDB.getReference()
                .child(FirebaseCommon.USER)
                .child(user.getUid());
    }

    protected void initViews() {
        signOutButton = findViewById(R.id.logout2);
        roleSwitch = findViewById(R.id.role);
        pastJob =findViewById(R.id.past_jobs_employer);
        profile = findViewById(R.id.profileEmployer);
        postJob = findViewById(R.id.post_job);
        ratingBar = findViewById(R.id.employerLandingRatingBar);
        FirebaseCommon.calculateRatingOfEmployer(user.getUid());
        userRef.child(FirebaseCommon.EMPLOYER_RATING)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Float rating = snapshot.getValue(Float.class);
                        if (rating != null) {
                            ratingBar.setRating(rating);
                            ratingBar.setIsIndicator(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("EmployerLanding", error.getMessage());
                    }
                });
    }

    protected void initListeners() {
        notificationButton.setOnClickListener(this::onClickNotifications);
        signOutButton.setOnClickListener(this::onClicklogout);
        roleSwitch.setOnClickListener(this::onClickRole);
        pastJob.setOnClickListener(this::onClickPastJob);
        profile.setOnClickListener(this::onClickProfile);
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
        Intent switchToEmployeeLanding = new Intent(EmployerLanding.this, EmployeeLanding.class);
        startActivity(switchToEmployeeLanding);
    }

    public void onClickProfile(View view) {
        Intent goProfile = new Intent(EmployerLanding.this, EmployerProfile.class);
        startActivity(goProfile);
    }

    public void onClickPostJob(View view) {
        Intent goToPostJob = new Intent(EmployerLanding.this, PostJob.class);
        startActivity(goToPostJob);
    }
    public void onClickPastJob(View view) {
        Intent goToPastJobs = new Intent(EmployerLanding.this, EmployerPastJobs.class);
        startActivity(goToPastJobs);
    }

    public void onClickNotifications(View view){
        DatabaseReference notif = firebaseDB.getReference().child(FirebaseConstants.USER).child(user.getUid()).child("notifications");
        // Read from the database the user notifications
        notif.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // outputs the current notification(s) for the user
                String message = (String) dataSnapshot.getValue();
                Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                toast.show();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // if nothing is read then there are no current notifications
                Toast toast = Toast.makeText(getApplicationContext(), "No current notifications",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }

}