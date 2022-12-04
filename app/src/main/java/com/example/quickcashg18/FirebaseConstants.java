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

    public static final String POSTER_RATING = "PosterRating";

    public static final String COMPLETER_RATING = "CompleterRating";

    public static final String NUM_JOBS_COMPLETED_BY_EMPLOYEES = "NumCompletedJobsByEmployees";

    public static final String NUM_JOBS_COMPLETED_BY_USER = "NumCompletedJobsByUser";

    public static final String NUM_RATINGS_AS_EMPLOYER = "NumRatingsAsEmployer";

    public static final String NUM_RATINGS_AS_EMPLOYEE = "NumRatingsAsEmployee";

    public static final String NO_PREFERENCE = "NoPreference";

    /**
     * Get the number of an employee's ratings by employers
     */
    private static void calculateNumRatingsAsEmployee(String userID) {
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FIREBASE_URL);
        DatabaseReference completedRef = firebaseDB.getReference()
                .child(PostJob.JOB_LIST)
                .child(COMPLETE_JOBS);
        DatabaseReference ratingNumRef = firebaseDB.getReference()
                .child(USER)
                .child(userID)
                .child(NUM_RATINGS_AS_EMPLOYEE);
        completedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int ratingNum = 0;
                for (DataSnapshot jobShot : snapshot.getChildren()) {
                    CompletedJob job = jobShot.getValue(CompletedJob.class);
                    if (job != null && job.getCompleterID().equals(userID) &&
                        snapshot.child(COMPLETER_RATING).exists()) {
                        ratingNum++;
                    }
                }
                ratingNumRef.setValue(ratingNum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseConstants", error.getMessage());
            }
        });
    }

    /**
     * Get the number of an employer's ratings by employees
     */
    public static void calculateNumRatingsAsEmployer(String userID) {
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FIREBASE_URL);
        DatabaseReference completedRef = firebaseDB.getReference()
                .child(PostJob.JOB_LIST)
                .child(COMPLETE_JOBS);
        DatabaseReference ratingNumRef = firebaseDB.getReference()
                .child(USER)
                .child(userID)
                .child(NUM_RATINGS_AS_EMPLOYER);
        completedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int ratingNum = 0;
                for (DataSnapshot jobShot : snapshot.getChildren()) {
                    CompletedJob job = jobShot.getValue(CompletedJob.class);
                    if (job != null && job.getPosterID().equals(userID) &&
                        jobShot.child(POSTER_RATING).exists()) {
                        ratingNum++;
                    }
                }
                ratingNumRef.setValue(ratingNum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseConstants", error.getMessage());
            }
        });
    }

    /**
     * Compute the number of a given event
     * @param userID The user ID associated with the denominator to be calculated
     * @param numKind The number of the kind of event
     */
    private static void runNumCalculator(String userID, String numKind) {
        if (numKind == null) {
            return;
        } else if (numKind.equals(NUM_RATINGS_AS_EMPLOYEE)) {
            calculateNumRatingsAsEmployee(userID);
        } else if (numKind.equals(NUM_RATINGS_AS_EMPLOYER)) {
            calculateNumRatingsAsEmployer(userID);
        }
    }

    /**
     * Compute the denominator for a rating calculation
     * @param userID The ID of the user to be rated
     * @param ratingKind The kind of rating to be performed (e.g. rating an employer)
     * @return The kind of denominator used for the rating calculation
     */
    private static String runNumCalculatorForRating(String userID, String ratingKind) {
        String numKind = null;
        if (ratingKind.equals(POSTER_RATING)) {
            numKind = NUM_RATINGS_AS_EMPLOYER;
        } else if (ratingKind.equals(COMPLETER_RATING)) {
            numKind = NUM_RATINGS_AS_EMPLOYEE;
        }
        if (numKind != null) {
            runNumCalculator(userID, numKind);
        }
        return numKind;
    }

    /**
     *
     * @param userID The ID of the user to be rated
     * @param ratingKind The kind of rating to be performed (e.g. rating an employer)
     * @param ratingToAdd The new rating to add to the user's previous ratings
     */
    private static void calculateRating(String userID, String ratingKind, float ratingToAdd) {
        String numKind = runNumCalculatorForRating(userID, ratingKind);
        DatabaseReference firebaseDB = FirebaseDatabase.getInstance(FIREBASE_URL).getReference();
        DatabaseReference userRef = firebaseDB.child(USER).child(userID);
        DatabaseReference numRef = userRef.child(numKind);
        DatabaseReference ratingRef = userRef.child(ratingKind);
        /*ratingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot prevRatingShot) {
                Float prevRatingCheck = prevRatingShot.getValue(Float.class);
                numRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot numShot) {
                        Integer numCheck = numShot.getValue(Integer.class);
                        if (prevRatingCheck != null && numCheck != null) {
                            Float prevRating = prevRatingCheck;
                            int num = numCheck;
                            ratingRef.
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        numRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot numShot) {
                // Get the denominator for rating computation
                Integer numCheck = numShot.getValue(Integer.class);
                if (numCheck != null) {
                    ratingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Get the user's previous total rating, and compute new rating
                            Float prevRatingCheck = snapshot.getValue(Float.class);
                            if (prevRatingCheck != null) {
                                int num = numCheck + 1;
                                float prevRating = prevRatingCheck;
                                ratingRef.setValue((prevRating + ratingToAdd) / num);
                                numRef.setValue(num);
                            } else {
                                ratingRef.setValue(ratingToAdd);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("FirebaseConstants", error.getMessage());
                        }
                    });
                } else {
                    // User didn't have a previous rating
                    ratingRef.setValue(ratingToAdd);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseConstants", error.getMessage());
            }
        });
    }

    public static void calculateRatingForEmployer(String userID, float rating) {
        calculateRating(userID, POSTER_RATING, rating);
        /*DatabaseReference firebaseDB = FirebaseDatabase.getInstance(FIREBASE_URL)
                .getReference();
        DatabaseReference ratingRef = firebaseDB.child(USER)
                .child(POSTER_RATING);
        DatabaseReference completedJobsRef = firebaseDB.child(PostJob.JOB_LIST)
                .child(COMPLETE_JOBS);
        completedJobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot completedJobsShot) {
                for (DataSnapshot job : completedJobsShot.getChildren()) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    public static void calculateRatingForEmployee(String userID, float rating) {
        calculateRating(userID, COMPLETER_RATING, rating);
    }

    /**
     * Set a user's rating of an employer for a completed job.
     */
    public static void setUserEmployerRating(CompletedJob job, float rating) {
        DatabaseReference firebaseDB = FirebaseDatabase.getInstance().getReference();
        firebaseDB.child(PostJob.JOB_LIST)
                .child(COMPLETE_JOBS)
                .child(job.getJobID())
                .child(POSTER_RATING)
                .setValue(rating);
        calculateRatingForEmployer(job.getPosterID(), rating);
    }

    /**
     * Set a user's rating of an employee for a completed job.
     */
    public static void setUserEmployeeRating(CompletedJob job, float rating) {
        DatabaseReference firebaseDB = FirebaseDatabase.getInstance().getReference();
        firebaseDB.child(PostJob.JOB_LIST)
                .child(COMPLETE_JOBS)
                .child(job.getJobID())
                .child(COMPLETER_RATING)
                .setValue(rating);
        calculateRatingForEmployee(job.getCompleterID(), rating);
    }

    /**
     * Ensures the database contains a number of useful keys.
     */
    public static void initDatabaseConstants() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference();
        dbRef.child("User").push();
    }

}
