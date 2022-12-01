package com.example.quickcashg18;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;

public class JobSearch extends ToolbarActivity {

    private ArrayList<Job> availableJobs;
    private JobAdapter adapter;
    private FirebaseDatabase firebaseDB;
    private DatabaseReference jobsRef;
    private DatabaseReference userRef;

    private SearchView enterJobTitle;
    private ListView listView;
    private Button importPref;
    private EditText enterDuration;
    private EditText enterTotalPay;
    private EditText enterUrgency;
    private EditText enterDistance;
    private Button selectLocation;
    private ActivityResultLauncher<Void> getLocation = registerForActivityResult(new LocationResultContract(),
            this::setEnteredLocation);
    private MyLocation location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);

        initViews();
        initDatabase();
        initListeners();
        refreshJobList();
        initJobList();
    }

    private void initViews() {
        enterJobTitle = findViewById(R.id.searchView);
        listView = findViewById(R.id.list_view);
        importPref = findViewById(R.id.importPreferencesJobSearch);
        enterDuration = findViewById(R.id.durationJobSearch);
        enterTotalPay = findViewById(R.id.totalPayJobSearch);
        enterUrgency = findViewById(R.id.urgencyJobSearch);
        enterDistance = findViewById(R.id.distanceJobSearch);
        selectLocation = findViewById(R.id.selectLocationJobSearch);
    }

    private void initListeners() {
        importPref.setOnClickListener(this::onClickImportPreferences);
        selectLocation.setOnClickListener(this::onClickSelectLocation);
    }

    private void initDatabase() {
        firebaseDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
        jobsRef = firebaseDB.getReference(PostJob.JOB_LIST).child(PostJob.INCOMPLETE_JOBS);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = firebaseDB.getReference(FirebaseConstants.USER).child(user.getUid());
    }

    /**
     * Initializes the job list as shown in the job search, along
     * with the adapter used to perform filtering.
     */
    private void initJobList() {
        adapter = new JobAdapter(this, R.layout.listed_job, R.id.slotJobTitleDescriptor, availableJobs);
        listView.setAdapter(adapter);
        enterJobTitle.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String jobTitle) {
                // Obtain the preferences
                String duration = getEnteredDuration();
                String totalPay = getEnteredTotalPay();
                String urgency = getEnteredUrgency();
                MyLocation location = getEnteredLocation();
                String distance = getEnteredDistance();
                EmployeePreferredJob pref = new EmployeePreferredJob(jobTitle, duration, totalPay,
                        urgency, location, distance);

                // Serialize the job preferences to be passed to the filter
                try {
                    ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
                    ObjectOutputStream out = new ObjectOutputStream(outBytes);
                    out.writeObject(pref);
                    String byteString = new String(Base64.getEncoder().encode(outBytes.toByteArray()));
                    adapter.getFilter().filter(byteString);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String jobTitle) {
                refreshJobList();
                return onQueryTextSubmit(jobTitle);
            }
        });
    }

    /**
     * Updates the job list with any new jobs from the database.
     * Useful for live updates of the incomplete jobs in case some
     * other user completes a job while the given user is searching.
     */
    private void refreshJobList() {
        availableJobs = new ArrayList();
        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot incompleteJobs) {
                for (DataSnapshot job : incompleteJobs.getChildren()) {
                    availableJobs.add(job.getValue(Job.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("JobSearch", error.getMessage());
            }
        });
    }

    public void onClickImportPreferences(View view) {
        DatabaseReference preferences = userRef.child(EmployeeProfile.PREFERENCES);
        preferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EmployeePreferredJob pref = snapshot.getValue(EmployeePreferredJob.class);
                if (pref != null) {
                    setEnteredJobTitle(pref.getJobTitle());
                    setEnteredDuration("" + pref.getDuration());
                    setEnteredTotalPay("" + pref.getTotalPay());
                    setEnteredUrgency(pref.getUrgency());
                    setEnteredDistance("" + pref.getMaxDistance());
                    setEnteredLocation(pref.getLocation());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("JobSearch", error.getMessage());
            }
        });
    }

    public void onClickSelectLocation(View view) {
        getLocation.launch(null);
    }

    public String getEnteredDuration() {
        return enterDuration.getText().toString().trim();
    }

    public String getEnteredTotalPay() {
        return enterTotalPay.getText().toString().trim();
    }

    public String getEnteredUrgency() {
        return enterUrgency.getText().toString().trim();
    }

    public String getEnteredDistance() {
        return enterDistance.getText().toString().trim();
    }

    public MyLocation getEnteredLocation() {
        return location;
    }

    public void setEnteredJobTitle(String jobTitle) {
        enterJobTitle.setQuery(jobTitle.trim(), true);
    }

    public void setEnteredDuration(String duration) {
        enterDuration.setText(duration.trim());
    }

    public void setEnteredTotalPay(String totalPay) {
        enterTotalPay.setText(totalPay.trim());
    }

    public void setEnteredUrgency(String urgency) {
        enterUrgency.setText(urgency.trim());
    }

    public void setEnteredDistance(String distance) {
        enterDistance.setText(distance.trim());
    }

    public void setEnteredLocation(MyLocation l) {
        location = l;
    }
}