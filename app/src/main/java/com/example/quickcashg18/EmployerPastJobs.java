package com.example.quickcashg18;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployerPastJobs extends ToolbarActivity {

    private static final String FIREBASEDB_URL = "https://quick-cash-g18-default-rtdb.firebaseio.com/";
    private FirebaseDatabase firebaseDB;
    private DatabaseReference userRef;
    private DatabaseReference postedJobsRef;
    private FirebaseUser user;
    private ListView postedJobs;
    private PostedJobsAdapter postedJobsAdapter;
    private ArrayList<PostedJob> postedJobsList;
    private HashMap<String, ArrayList<String>> jobToApplicants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_jobs);

        //Button payJob =findViewById(R.id.paymentPage);
        //payJob.setOnClickListener(this::onClickPayJob);


        //initialize the database instance and creating references for the job details
        initDatabase();
        initViews();
        refreshPostedJobs();
    }


    protected void initDatabase() {
        //initialize the database and the references relating to the job details
        firebaseDB = FirebaseDatabase.getInstance(FIREBASEDB_URL);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = firebaseDB.getReference(FirebaseConstants.USER).child(user.getUid());
        postedJobsRef = firebaseDB.getReference(PostJob.JOB_LIST).child(PostJob.INCOMPLETE_JOBS);
    }

    private void initViews() {
        postedJobs = findViewById(R.id.postedJobsList);
    }

    private void updatePostedJobsList() {
        postedJobsAdapter = new PostedJobsAdapter(this, postedJobsList);
        postedJobs.setAdapter(postedJobsAdapter);
    }

    private void refreshPostedJobs() {
        postedJobsList = new ArrayList<>();
        jobToApplicants = new HashMap<>();
        postedJobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot incompleteJobs) {
                for (DataSnapshot jobShot : incompleteJobs.getChildren()) {
                    PostedJob job = jobShot.getValue(PostedJob.class);
                    if (job != null &&
                            job.getPosterID().equals(user.getUid())) {
                        postedJobsList.add(job);
                        ArrayList<String> applicants = new ArrayList<String>();
                        for (DataSnapshot applicant : incompleteJobs.child(job.getJobID())
                                .child(JobSearch.APPLICANTS).getChildren()) {
                            applicants.add(applicant.getValue(String.class));
                        }
                        jobToApplicants.put(job.getJobID(), applicants);
                    }
                }
                updatePostedJobsList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EmployerPastJobs", error.getMessage());
                updatePostedJobsList();
            }
        });
    }

    public void onClickPayJob(View view) {
        Intent payment = (new Intent(EmployerPastJobs.this, payment_portal.class));
        startActivity(payment);
    }

    private class PostedJobsAdapter extends ArrayAdapter<PostedJob> {

        public PostedJobsAdapter(@NonNull Context context, @NonNull ArrayList<PostedJob> objects) {
            super(context, R.layout.job_slot, R.id.slotJobTitleDescriptor, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.job_slot, parent, false);
            }

            TextView title = convertView.findViewById(R.id.slotJobTitleDescriptor);
            TextView pay = convertView.findViewById(R.id.slotTotalPayDescriptor);
            TextView duration = convertView.findViewById(R.id.slotDurationDescriptor);
            TextView urgency = convertView.findViewById(R.id.slotUrgencyDescriptor);

            PostedJob job = getItem(position);

            title.setText(job.getJobTitle());
            pay.setText(String.valueOf(job.getTotalPay()));
            duration.setText(String.valueOf(job.getDuration()));
            urgency.setText(String.valueOf(job.getUrgency()));

            return convertView;
        }

    }

    /*
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
    }*/
}
