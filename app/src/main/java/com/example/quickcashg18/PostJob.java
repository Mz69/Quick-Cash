package com.example.quickcashg18;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostJob<FirebaseUser> extends AppCompatActivity {

        private static final String FIREBASEDB_URL = "https://quick-cash-g18-default-rtdb.firebaseio.com/";
        private FirebaseDatabase firebaseJobDB;
        private DatabaseReference jobName;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_post_job);

            // button for adding a job
            Button addJob = findViewById(R.id.JobButton);
            addJob.setOnClickListener(this::onClickAddJob);


            //initialize the database instance and creating references for the job details
            initializeDatabase();

        }


    protected void initializeDatabase() {
            //initialize the database and the references relating to the job details
            firebaseJobDB = FirebaseDatabase.getInstance(FIREBASEDB_URL);
            jobName = firebaseJobDB.getReference("Jobs");
        }

        // getters for the job details
        protected String getJobName() {
            EditText jobName = findViewById(R.id.JobName);
            return jobName.getText().toString().trim();
        }

        protected String getLocation() {
            EditText location = findViewById(R.id.location);
            return location.getText().toString().trim();
        }

        protected String getTimeFrame() {
            EditText timeFrame = findViewById(R.id.timeFrame);
            return timeFrame.getText().toString().trim();
        }

        protected String getUrgency() {
            EditText urgency = findViewById(R.id.urgency);
            return urgency.getText().toString().trim();
        }

        protected String getSalary() {
            EditText salary = findViewById(R.id.salary);
            return salary.getText().toString().trim();
        }

        // method checks if all the job details have been entered
        protected boolean isJobValid() {
            if (!getJobName().isEmpty() && !getLocation().isEmpty() && !getTimeFrame().isEmpty() && !getUrgency().isEmpty() &&  !getSalary().isEmpty()) {
                return true;
            }
            else {
                return false;
            }
        }

        // methods to save job details in firebase database
        protected Task<Void> saveJobtoFirebase(String JobName, String Location, String TimeFrame, String Urgency, String Salary) {
            // setting the job name in listings
            jobName.child(JobName).push();
            // saving all the other job information
            jobName.child(JobName).child("Location").push().setValue(Location);
            jobName.child(JobName).child("TimeFrame").push().setValue(TimeFrame);
            jobName.child(JobName).child("Urgency").push().setValue(Urgency);
            jobName.child(JobName).child("Salary").push().setValue(Salary);
            return null;
        }

        public void onClickAddJob(View view) {
            String jobName = getJobName();
            String location = getLocation();
            String timeFrame = getTimeFrame();
            String urgency = getUrgency();
            String salary = getSalary();

            // check to see if any of the job information wasn't provided
            if (isJobValid()) {
                // Saving the job details to the database
                saveJobtoFirebase(jobName,location,timeFrame,urgency,salary);
                Toast successMsg = Toast.makeText(getApplicationContext(), "Job Created Successfully", Toast.LENGTH_LONG);
                successMsg.show();
                // switching back to the employer landing screen after creating the job is posted
                Intent employerLandingIntent = new Intent(this, employer_landing.class);
                startActivity(employerLandingIntent);
            }
            else if(!isJobValid()) {
                // displaying an error message job information is not fully entered
                Toast errorMsg = Toast.makeText(getApplicationContext(), "Not all fields are filled out", Toast.LENGTH_LONG);
                errorMsg.show();
            }

        }

    }