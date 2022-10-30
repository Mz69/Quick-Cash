package com.example.quickcashg18;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.BreakIterator;

public class employer_job_creation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer_job_creation);
        Button createJob = findViewById(R.id.button2);
        createJob.setOnClickListener(this::onClickCreateJob);
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
            Toast.makeText(getApplicationContext(), "Please complete all feilds", Toast.LENGTH_SHORT).show();
        }
        //if all fields are filled create a job object
        else {
            Job createjob= new Job (personName,jobDescription,durationInHrs,hourlyPay,urgency);

            //create a database instance


        }

    }
}

