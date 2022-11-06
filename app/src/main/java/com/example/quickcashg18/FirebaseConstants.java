package com.example.quickcashg18;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    /**
     * Ensures the database contains a number of useful keys.
     */
    public static void initDatabaseConstants() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference();
        dbRef.child("User").push();
    }

}
