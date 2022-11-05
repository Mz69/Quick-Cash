package com.example.quickcashg18;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConstants {

    public static final String FIREBASE_URL = "https://quick-cash-g18-default-rtdb.firebaseio.com/";
    public static final String USER = "User";

    public static void initDatabaseConstants() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference();
        dbRef.child("User").push();
    }

}
