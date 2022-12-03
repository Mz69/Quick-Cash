package com.example.quickcashg18;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;



public class PastJobs extends ToolbarActivity {
    private TextView JobTitle;
    private TextView JobPay;
    private TextView JobDuration;
    private TextView JobUrgency;
    private ArrayList<Job> availableJobs;
    private JobAdapter adapter;
    private ListView listView;
    private Button PayForJob;
    private Button LoadJobs;
    private SearchView enterUser;


    private static final String FIREBASEDB_URL = "https://quick-cash-g18-default-rtdb.firebaseio.com/";
    private FirebaseDatabase firebaseDB;
    private DatabaseReference jobsRef;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_jobs);

        //initialize the database instance and creating references for the job details
        initializeDatabase();
        initViews();
        setListeners();

    }

    private void initViews() {
        JobTitle = findViewById(R.id.JobTitleTV);
        JobPay = findViewById(R.id.JobPayTV);
        JobDuration = findViewById(R.id.JobDurationTV);
        JobUrgency = findViewById(R.id.UrgencyTV);
        PayForJob = findViewById(R.id.PayForJob);
        LoadJobs = findViewById(R.id.loadJob);
    }

    private void setListeners() {
        PayForJob.setOnClickListener(this::onClickPayJob);
        LoadJobs.setOnClickListener(this::onClickJobLoad);
        }

    private void onClickPayJob(View view) {
        String payAmount=JobPay.getText().toString();
        Intent payment = (new Intent(PastJobs.this, payment_portal.class));
        payment.putExtra("pay_key",payAmount);
        startActivity(payment);
    }

    private void onClickJobLoad(View view) {
        String user= String.valueOf(userRef);
        getJobsFromDatabase(user);
    }

    private void getJobsFromDatabase(String user) {
        final Query jobQuery=firebaseDB.getReference(PostJob.JOB_LIST).child(PostJob.INCOMPLETE_JOBS);
        jobQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot incompleteJobs) {
                Job jobs;
                for (DataSnapshot job : incompleteJobs.getChildren()) {
                    jobs=job.getValue(Job.class);
                    if(jobs!=null){
                        JobTitle.setText(String.format("Job Title: %s",jobs.getJobTitle()));
                        JobPay.setText(String.format("Job Pay: %s",String.valueOf(jobs.getTotalPay())));
                        JobDuration.setText(String.format("Job Duration: %s",jobs.getDuration()));
                        JobUrgency.setText(String.format("Job Urgency: %s",jobs.getUrgency()));
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("JobSearch", error.getMessage());
            }
        });
    }
    protected void initializeDatabase() {
        //initialize the database and the references relating to the job details
        firebaseDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
        jobsRef = firebaseDB.getReference(PostJob.JOB_LIST).child(PostJob.INCOMPLETE_JOBS);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = firebaseDB.getReference(FirebaseConstants.USER).child(user.getUid());
    }
}

