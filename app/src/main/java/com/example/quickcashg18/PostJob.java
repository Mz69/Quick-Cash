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
        jobDBRef = firebaseJobDB.getReference("Jobs");
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

    protected String getDurationHours() {
        String durationHours = findViewById(R.id.durationHours).toString().trim();
        return durationHours;
    }

    protected String getDurationMins() {
        String durationMins = findViewById(R.id.durationHours).toString().trim();
        return durationMins;
    }

    protected String getUrgency() {
        EditText urgency = findViewById(R.id.urgency);
        return urgency.getText().toString().trim();
    }

    protected String getTotalPay() {
        EditText totalPay = findViewById(R.id.totalPay);
        return totalPay.getText().toString().trim();
    }

    protected String getDescription() {
        EditText description = findViewById(R.id.jobDescription);
        return description.getText().toString().trim();
    }

    public boolean isValidJobName() {
        return !getJobName().isEmpty();
    }

    public boolean isValidTotalPay() {
        return getTotalPay().matches("[0-9]+(\\.[0-9]{1,2}){0,1}");
    }

    public boolean isValidDurationHours() {
        return getDurationHours().matches("[0-9]{0,}");
    }

    public boolean isValidDurationMins() {
        return getDurationMins().matches("[0-9]{0,}");
    }

    public boolean isValidUrgency() {
        return getUrgency().equalsIgnoreCase("Urgent") || getUrgency().equalsIgnoreCase("Not Urgent");
    }

    public boolean isValidLocation() {
        return getLocation() != null;
    }

    // method checks if all the job details have been entered
    protected boolean isJobValid() {
        // validating that all the fields have been entered correctly
        return isValidJobName() && isValidTotalPay() && isValidDurationHours() && isValidDurationMins() && isValidUrgency() && isValidLocation();
    }

    // methods to save job details in firebase database
    // setting the job name in listings
    protected void saveJobtoFirebase(Job job) {
        String jobName = job.getJobName();
        jobDBRef.child(jobName).push();
        // saving all the other job information
        jobDBRef.child(jobName).child("Location").setValue(job.getLocation());
        jobDBRef.child(jobName).child("Duration").setValue(job.getDuration());
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
            return;
        }

        String jobName = getJobName();
        Location location = getLocation();
        int durationHours = Integer.parseInt(getDurationHours());
        int durationMins = Integer.parseInt(getDurationMins());
        String urgency = getUrgency();
        double totalPay = Double.parseDouble(getTotalPay());
        String description = getDescription();
        Job job = new Job(jobName, location, durationHours, durationMins, totalPay, urgency, description);

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
