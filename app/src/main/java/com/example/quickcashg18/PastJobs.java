package com.example.quickcashg18;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class PastJobs extends AppCompatActivity {

    private static final String FIREBASEDB_URL = "https://quick-cash-g18-default-rtdb.firebaseio.com/";
    private FirebaseDatabase firebaseJobDB;
    private DatabaseReference jobName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_jobs);

        Button payJob =findViewById(R.id.paymentPage);
        payJob.setOnClickListener(this::onClickPayJob);


        //initialize the database instance and creating references for the job details
        initializeDatabase();

    }


    protected void initializeDatabase() {
        //initialize the database and the references relating to the job details
        firebaseJobDB = FirebaseDatabase.getInstance(FIREBASEDB_URL);
        jobName = firebaseJobDB.getReference("Jobs/Incomplete");
    }

    public void onClickPayJob(View view) {
        Intent payment= (new Intent(PastJobs.this, payment_portal.class));
        startActivity(payment);
    }
}
