package com.example.quickcashg18;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;

public class employer_job_creation extends AppCompatActivity {
    private static final String FIREBASE_DATABASE_URL ="https://quick-cash-g18-default-rtdb.firebaseio.com/";
    private FirebaseDatabase firebaseDB;
    private DatabaseReference firebaseDBRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer_job_creation);
        Button createJob = findViewById(R.id.button2);
        createJob.setOnClickListener(this::onClickCreateJob);
    }
    protected void initializedDatabase(){
        firebaseDB=FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
        firebaseDBRef=firebaseDB.getReference("https://quick-cash-g18-default-rtdb.firebaseio.com/Job");
    }

    private void onClickCreateJob(View view) {
        EditText editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        EditText editTextTextPersonName2 = findViewById(R.id.editTextTextPersonName2);
        EditText editTextTextPersonName3 = findViewById(R.id.editTextTextPersonName3);
        EditText editTextTextPersonName4 = findViewById(R.id.editTextTextPersonName4);
        EditText editTextTextPersonName5 = findViewById(R.id.editTextTextPersonName5);


        String personName = editTextTextPersonName.getText().toString();
        String jobDescription = editTextTextPersonName2.getText().toString();
        String durationInHrs = editTextTextPersonName3.getText().toString();
        String hourlyPay = editTextTextPersonName4.getText().toString();
        String urgency = editTextTextPersonName5.getText().toString();
        //check for any empty fields
        if (editTextTextPersonName.getText().toString().isEmpty() || editTextTextPersonName2.getText().toString().isEmpty() ||
                editTextTextPersonName3.getText().toString().isEmpty() || editTextTextPersonName4.getText().toString().isEmpty() ||
                editTextTextPersonName5.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please complete all fields", Toast.LENGTH_SHORT).show();
        }
        //if all fields are filled create a job object
        else {

            Job createjob= new Job (personName,jobDescription,durationInHrs,hourlyPay,urgency);
            Map<String, Job> jobUpdates = new HashMap<>();
            jobUpdates.put(personName, createjob);
            DatabaseReference jobBoard=firebaseDBRef.child(personName);
            DatabaseReference newJob=jobBoard.push();
            newJob.setValue(jobUpdates);

        }

    }
}

