package com.example.quickcashg18;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

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

    private DatabaseReference userRef;
    private DatabaseReference jobDBRef;
    private MyLocation selectedLocation;
    Toast errorMsg;
    private ActivityResultLauncher<Void> getLocation = registerForActivityResult(new LocationResultContract(),
            this::setSelectedLocation);

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
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = firebaseDB.getReference(FirebaseConstants.USER)
                .child(user.getUid());
        jobDBRef = firebaseDB.getReference(JOB_LIST).child(INCOMPLETE_JOBS);
    }

    // getters for the job details
    protected String getJobTitle() {
        EditText jobTitle = findViewById(R.id.jobTitle);
        return jobTitle.getText().toString().trim();
    }

    protected MyLocation getSelectedLocation() {
        return selectedLocation;
    }

    protected void setSelectedLocation(MyLocation l) {
            selectedLocation = l;
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
        return getUrgency().equalsIgnoreCase("Urgent") || getUrgency().equalsIgnoreCase("Not Urgent");
    }

    public boolean isValidLocation() {
        return getSelectedLocation() != null;
    }

    // method checks if all the job details have been entered
    protected boolean isJobValid() {
        // validating that all the fields have been entered correctly
        return isValidJobTitle() && isValidTotalPay() && isValidDuration() && isValidUrgency() && isValidLocation();
    }

    // methods to save job details in firebase database
    // setting the job name in listings
    protected void saveJobtoFirebase(PostedJob job) {
        jobDBRef.child(job.getJobID()).setValue(job);
    }

    public void onClickImportPreferences(View view) {
        DatabaseReference preferences = userRef.child(EmployerProfile.PREFERENCES);

        preferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EmployerPreferredJob prefJob = snapshot.getValue(EmployerPreferredJob.class);
                if (prefJob != null) {
                    setJobTitle(prefJob.getJobTitle());
                    setTotalPay("" + prefJob.getTotalPay());
                    setDuration("" + prefJob.getDuration());
                    setUrgency(prefJob.getUrgency());
                    setSelectedLocation(prefJob.getMyLocation());
                }
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
        MyLocation location = getSelectedLocation();
        String urgency = getUrgency();
        double totalPay = Double.parseDouble(getTotalPay());
        double duration = Double.parseDouble(getDuration());
        String posterID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        PostedJob job = new PostedJob(jobTitle, duration, totalPay, urgency, location, posterID);
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
