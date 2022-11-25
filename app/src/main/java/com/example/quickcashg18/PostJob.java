package com.example.quickcashg18;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Activity for creating and posting a job.
 */
/*
This class was originally created by Riley when working with his old
group before he got moved to this group. He has adapted it for this project.
 */
public class PostJob extends ToolbarActivity {

    private FirebaseDatabase firebaseJobDB;
    private DatabaseReference userRef;
    private DatabaseReference jobDBRef;
    private MyLocation location;
    Toast errorMsg;
    private ActivityResultLauncher<Void> getLocation = registerForActivityResult(new LocationResultContract(),
            this::setLocation);

    public static final String JOB_LIST = "Jobs";
    public static final String INCOMPLETE_JOBS = "Incomplete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        Button importPref = findViewById(R.id.importEmployerPreferences);
        importPref.setOnClickListener(this::onClickImportPreferences);
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = firebaseJobDB.getReference(FirebaseConstants.USER)
                .child(user.getUid());
        jobDBRef = firebaseJobDB.getReference(JOB_LIST).child(INCOMPLETE_JOBS);
    }

    // getters for the job details
    protected String getJobTitle() {
        EditText jobTitle = findViewById(R.id.jobTitle);
        return jobTitle.getText().toString().trim();
    }

    protected MyLocation getLocation() {
        return location;
    }

    protected void setLocation(MyLocation l) {
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

    protected void setJobTitle(String jobTitle) {
        EditText titleField = findViewById(R.id.jobTitle);
        titleField.setText(jobTitle.trim());
    }

    protected void setTotalPay(String totalPay) {
        EditText payField = findViewById(R.id.totalPay);
        payField.setText(totalPay.trim());
    }

    protected void setDuration(String duration) {
        EditText durationField = findViewById(R.id.duration);
        durationField.setText(duration.trim());
    }

    protected void setUrgency(String urgency) {
        EditText urgencyField = findViewById(R.id.urgency);
        urgencyField.setText(urgency.trim());
    }


    protected void setDescription(String description) {
        EditText descriptionField = findViewById(R.id.jobDescription);
        descriptionField.setText(description.trim());
    }

    public boolean isValidJobTitle() {
        return !getJobTitle().isEmpty();
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
        return isValidJobTitle() && isValidTotalPay() && isValidDuration() && isValidUrgency() && isValidLocation();
    }

    // methods to save job details in firebase database
    // setting the job name in listings
    protected void saveJobtoFirebase(Job job) {
        jobDBRef.child(job.getJobID()).setValue(job);
    }

    public void onClickImportPreferences(View view) {
        DatabaseReference preferences = userRef.child(EmployerProfile.PREFERENCES);

        preferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setJobTitle(snapshot.child(EmployerProfile.JOB_TITLE).getValue(String.class));
                setTotalPay(snapshot.child(EmployerProfile.TOTAL_PAY).getValue(String.class));
                setDuration(snapshot.child(EmployerProfile.DURATION).getValue(String.class));
                setUrgency(snapshot.child(EmployerProfile.URGENCY).getValue(String.class));
                setDescription(snapshot.child(EmployerProfile.DESCRIPTION).getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PostJob", error.getMessage());
            }
        });
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

        String jobTitle = getJobTitle();
        MyLocation location = getLocation();
        String urgency = getUrgency();
        double totalPay = Double.parseDouble(getTotalPay());
        double duration = Double.parseDouble(getDuration());
        String description = getDescription();
        String posterID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Job job = new Job(jobTitle, location, duration, totalPay, urgency, description, posterID);

        // Saving the job details to the database
        saveJobtoFirebase(job);
        Toast successMsg = Toast.makeText(getApplicationContext(), "Job Created Successfully", Toast.LENGTH_LONG);
        successMsg.show();

        Alert jobAlert = new Alert();
        //jobAlert.notifyEmployee(job);
        // switching back to the employer landing screen after the job is posted
        Intent employerLandingIntent = new Intent(this, EmployerLanding.class);
        startActivity(employerLandingIntent);
    }

}
