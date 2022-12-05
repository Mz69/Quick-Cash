package com.example.quickcashg18;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

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
import java.util.List;

/**
 * Handles all job searching by employees.
 * At present, the user must type in the job search field in order
 * for all of their filters to be applied.
 */
public class JobSearch extends ToolbarActivity {

    private ArrayList<PostedJob> availableJobs;
    private JobAdapter adapter;
    private DatabaseReference jobsRef;
    private DatabaseReference userRef;
    private DatabaseReference userPrefRef;

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
    private MyLocation selectedLocation;
    public static final String APPLICANTS = "Applicants";
    private static final String JOB_SEARCH_LOG = "JobSearch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);

        initDatabase();
        initViews();
        initLocation();
        initListeners();

        refreshJobList();
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
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FirebaseCommon.FIREBASE_URL);
        jobsRef = firebaseDB.getReference(PostJob.JOB_LIST).child(PostJob.INCOMPLETE_JOBS);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = firebaseDB.getReference(FirebaseCommon.USER).child(user.getUid());
        userPrefRef = userRef.child(EmployeeProfile.PREFERENCES);
    }

    private void initLocation() {
        userRef.child(MapsActivity.CURRENT_LOCATION).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                selectedLocation = snapshot.getValue(MyLocation.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(JOB_SEARCH_LOG, error.getMessage());
            }
        });
    }

    /**
     * Initializes the job list as shown in the job search, along
     * with the adapter used to perform filtering.
     */
    private void initJobList() {
        adapter = new JobSearchAdapter(this, R.layout.incomplete_job_for_search,
                R.id.slotJobTitleDescriptor, availableJobs);
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
        synchronized (availableJobs) {
            jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot incompleteJobs) {
                    for (DataSnapshot job : incompleteJobs.getChildren()) {
                        availableJobs.add(job.getValue(PostedJob.class));
                    }
                    initJobList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(JOB_SEARCH_LOG, error.getMessage());
                }
            });
        }
    }


    public void onClickImportPreferences(View view) {
        userPrefRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EmployeePreferredJob pref = snapshot.getValue(EmployeePreferredJob.class);
                if (pref != null) {
                    setEnteredJobTitle(pref.getJobTitle());
                    setEnteredDuration("" + pref.getDuration());
                    setEnteredTotalPay("" + pref.getTotalPay());
                    setEnteredUrgency(pref.getUrgency());
                    setEnteredDistance("" + pref.getMaxDistance());
                    setEnteredLocation(pref.getMyLocation());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(JOB_SEARCH_LOG, error.getMessage());
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
        return selectedLocation;
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
        selectedLocation = l;
    }

    public static void applyToJob(PostedJob job, String userID) {
        // find the job in the database
        // add the applicant to the list
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FirebaseCommon.FIREBASE_URL);
        DatabaseReference applicantsRef = firebaseDB.getReference()
                .child(PostJob.JOB_LIST)
                .child(PostJob.INCOMPLETE_JOBS)
                .child(job.getJobID())
                .child(APPLICANTS);
        applicantsRef.child(userID).setValue(userID);
    }

    private class JobSearchAdapter extends JobAdapter {

        public JobSearchAdapter(@NonNull Context context, int resource, int textViewResourceId,
                                @NonNull List<PostedJob> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View slot = getJobSlot(position, convertView, parent);
            Button apply = slot.findViewById(R.id.applyToJob);
            apply.setOnClickListener(getOnClickApplyToJob(getItem(position)));

            return slot;
        }

        /**
         * Allow users to apply to jobs. Note that we put this logic
         * within JobSearchAdapter and not the parent public class
         * JobAdapter to ensure we wouldn't have to give random instances
         * of JobAdapter unnecessary access to the database and user instances!
         */
        /*
        Complete Jobs
            jobID
                People who did it
                    ...
                Paid for?
        User
            Completed Jobs with ID
         */
        public View.OnClickListener getOnClickApplyToJob(PostedJob job) {
            return v -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                applyToJob(job, user.getUid());
                Toast.makeText(getContext(), "Applied successfully", Toast.LENGTH_LONG).show();
            };
        }

        /**
         * Employees need to be able to do the following:
         * List all of their jobs
         * In progress
         * Completed
         * Applied for
         * See if a previous job is paid for. If it has been paid for,
         the payment total should be added to the employee's total income.
         * Click a button to rate the employer.
         For these reasons, the job from the employee's perspective should contain
         the following data:
         * The PostedJob object, as to access the information about the job
         and display its details. Must include the employer's ID so that
         the employer can be rated!
         * A boolean stating whether or not it's been paid for.
         * Employers need to be able to do the following:
         * List all of their previous jobs
         * Previously posted
         * Completed
         * Currently available
         * Pay employees for completing jobs.
         * Rate employees.
         For these reasons, the job from the employer's perspective
         should have the following data:
         * The PostedJob object.
         * The user's ID.
         * A boolean stating whether or not it's been paid for.
         */
    }

}
