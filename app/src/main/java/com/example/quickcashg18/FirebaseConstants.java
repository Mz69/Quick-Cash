package com.example.quickcashg18;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This class defines a number of constants in relation to the
 * Firebase database.
 */
public class FirebaseConstants {

    // Created according to guidelines given by SonarQube
    private FirebaseConstants() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The URL to the Firebase database.
     */
    public static final String FIREBASE_URL = "https://quick-cash-g18-default-rtdb.firebaseio.com/";

    /**
     * The name of the key in the database corresponding to the folder of users.
     */
    public static final String USER = "User";

    public static final String USER_INCOME = "Income";

    public static final String COMPLETE_JOBS = "Complete";

    public static final String EMPLOYER_RATING = "EmployerRating";

    public static final String EMPLOYEE_RATING = "EmployeeRating";

    /**
     * Compute the given employer's total rating by averaging their
     * ratings per job
     */
    public static void calculateRatingOfEmployer(String userID) {
        DatabaseReference firebaseDB = FirebaseDatabase.getInstance(FIREBASE_URL).getReference();
        DatabaseReference completedRef = firebaseDB.child(PostJob.JOB_LIST)
                .child(COMPLETE_JOBS);
        DatabaseReference ratingRef = firebaseDB.child(USER)
                .child(EMPLOYER_RATING);
        completedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int ratingNum = 0;
                float ratingVal = 0;
                for (DataSnapshot jobShot : snapshot.getChildren()) {
                    CompletedJob job = jobShot.getValue(CompletedJob.class);
                    if (job != null && job.getPosterID().equals(userID) &&
                        jobShot.child(EMPLOYER_RATING).exists()) {
                        ratingVal += jobShot.child(EMPLOYER_RATING).getValue(Float.class);
                        ratingNum++;
                    }
                }
                if (ratingNum == 0) {
                    ratingNum++;
                }
                ratingRef.setValue(ratingVal / ratingNum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseConstants", error.getMessage());
            }
        });
    }

    /**
     * Compute the given employee's total rating by averaging their
     * ratings per job
     */
    public static void calculateRatingOfEmployee(String userID) {
        DatabaseReference firebaseDB = FirebaseDatabase.getInstance(FIREBASE_URL).getReference();
        DatabaseReference completedRef = firebaseDB.child(PostJob.JOB_LIST)
                .child(COMPLETE_JOBS);
        DatabaseReference ratingRef = firebaseDB.child(USER)
                .child(EMPLOYEE_RATING);
        completedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int ratingNum = 0;
                float ratingVal = 0;
                for (DataSnapshot jobShot : snapshot.getChildren()) {
                    CompletedJob job = jobShot.getValue(CompletedJob.class);
                    if (job != null && job.getCompleterID().equals(userID) &&
                            jobShot.child(EMPLOYEE_RATING).exists()) {
                        ratingVal += jobShot.child(EMPLOYEE_RATING).getValue(Float.class);
                        ratingNum++;
                    }
                }
                if (ratingNum == 0) {
                    ratingNum++;
                }
                ratingRef.setValue(ratingVal / ratingNum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseConstants", error.getMessage());
            }
        });
    }

    /**
     * Set a user's rating of an employer for a completed job.
     */
    public static void employeeRateEmployer(CompletedJob job, float rating) {
        DatabaseReference firebaseDB = FirebaseDatabase.getInstance().getReference();
        firebaseDB.child(PostJob.JOB_LIST)
                .child(COMPLETE_JOBS)
                .child(job.getJobID())
                .child(EMPLOYER_RATING)
                .setValue(rating);
        calculateRatingOfEmployer(job.getPosterID());
    }

    /**
     * Set a user's rating of an employee for a completed job.
     */
    public static void employerRateEmployee(CompletedJob job, float rating) {
        DatabaseReference firebaseDB = FirebaseDatabase.getInstance().getReference();
        firebaseDB.child(PostJob.JOB_LIST)
                .child(COMPLETE_JOBS)
                .child(job.getJobID())
                .child(EMPLOYEE_RATING)
                .setValue(rating);
        calculateRatingOfEmployee(job.getCompleterID());
    }

    /**
     * Ensures the database contains a number of useful keys.
     */
    public static void initDatabaseConstants() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference();
        dbRef.child("User").push();
    }

}
