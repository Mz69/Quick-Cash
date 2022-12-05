package com.example.quickcashg18;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecommedEmployees extends AppCompatActivity {

    private DatabaseReference userRef;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_recommendation);
        initDatabase();
    }
    protected void initDatabase() {
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FirebaseCommon.FIREBASE_URL);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = firebaseDB.getReference()
                .child(FirebaseCommon.USER)
                .child(user.getUid());
    }

}