package com.example.quickcashg18;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Activity for creating and posting a job.
 */
/*
This class was originally created by Riley when working with his old
group before he got moved to this group. He has adapted it for this project.
 */
public class PostJob extends AppCompatActivity {

    private FirebaseDatabase firebaseJobDB;
    private DatabaseReference jobDBRef;
    private Location location;
    Toast errorMsg;
    private ActivityResultLauncher<Void> getLocation = registerForActivityResult(new LocationResultContract(),
            this::setLocation);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        // button for adding a job
        Button addJob = findViewById(R.id.JobButton);
        addJob.setOnClickListener(this::onClickAddJob);
        Button selectLocation = findViewById(R.id.jobOpenLocationSelect);
        selectLocation.setOnClickListener(this::onClickGetLocation);

        //initialize the database instance and creating references for the job details
        initializeDatabase();

    }

    protected void initializeDatabase() {
        //initialize the database and the references relating to the job details
        firebaseJobDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
        jobDBRef = firebaseJobDB.getReference("Jobs").child("Incomplete");
    }

    // getters for the job details
    protected String getJobName() {
        EditText getJobName = findViewById(R.id.JobName);
        return getJobName.getText().toString().trim();
    }

    protected Location getLocation() {
        return location;
    }

    protected void setLocation(Location l) {
            location = l;
    }

    protected String getDuration() {
        EditText duration = findViewById(R.id.duration);
        return duration.getText().toString();
    }


    protected String getUrgency() {
        EditText urgency = findViewById(R.id.urgency);
        return urgency.getText().toString();
    }

    protected String getTotalPay() {
        EditText totalPay = findViewById(R.id.totalPay);
        return totalPay.getText().toString();
    }

    protected String getDescription() {
        EditText description = findViewById(R.id.jobDescription);
        return description.getText().toString();
    }

    public boolean isValidJobName() {
        return !getJobName().isEmpty();
    }

    public boolean isValidTotalPay() {
        return Validation.isNumeric(getTotalPay());
    }

    public boolean isValidDuration() {
        return Validation.isNumeric(getDuration());
    }

    public boolean isValidUrgency() {
        if (!(getUrgency().equalsIgnoreCase("Urgent") || getUrgency().equalsIgnoreCase("Not Urgent"))) {
            System.out.println("Failed urgency");
        }
        return getUrgency().equalsIgnoreCase("Urgent") || getUrgency().equalsIgnoreCase("Not Urgent");
    }

    public boolean isValidLocation() {
        if (getLocation == null) {
            System.out.println("Failed location");
        }
        return getLocation() != null;
    }

    // method checks if all the job details have been entered
    protected boolean isJobValid() {
        // validating that all the fields have been entered correctly
        return isValidJobName() && isValidTotalPay() && isValidDuration() && isValidUrgency() && isValidLocation();
    }

    // methods to save job details in firebase database
    // setting the job name in listings
    protected void saveJobtoFirebase(Job job) {
        String jobName = job.getJobName();
        jobDBRef.child(jobName).push();
        // saving all the other job information
        jobDBRef.child(jobName).child("Location").setValue(job.getLocation());
        jobDBRef.child(jobName).child("Duration in Hours").setValue(job.getDuration());
        jobDBRef.child(jobName).child("Urgency").setValue(job.getUrgency());
        jobDBRef.child(jobName).child("Total Pay").setValue(job.getTotalPay());
        jobDBRef.child(jobName).child("Description").setValue(job.getDescription());
    }

    public void onClickGetLocation(View view) {
        getLocation.launch(null);
    }

    public void onClickAddJob(View view) {
        if (!isJobValid()) {
            errorMsg = Toast.makeText(this, "Error: Incorrect job fields entered!", Toast.LENGTH_LONG);
            errorMsg.show();
            return;
        }

        String jobName = getJobName();
        Location location = getLocation();
        String urgency = getUrgency();
        double totalPay = Double.parseDouble(getTotalPay());
        double duration = Double.parseDouble(getDuration());
        String description = getDescription();

        Job job = new Job(jobName, location, duration, totalPay, urgency, description);

        // Saving the job details to the database
        saveJobtoFirebase(job);
        Toast successMsg = Toast.makeText(getApplicationContext(), "Job Created Successfully", Toast.LENGTH_LONG);
        successMsg.show();

        Alert jobAlert = new Alert();
        jobAlert.notifyEmployee(job);
        // switching back to the employer landing screen after the job is posted
        Intent employerLandingIntent = new Intent(this, EmployerLanding.class);
        startActivity(employerLandingIntent);
    }

}
