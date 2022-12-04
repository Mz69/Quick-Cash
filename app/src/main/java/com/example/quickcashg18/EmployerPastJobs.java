package com.example.quickcashg18;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private HashMap<String, CircularNode<String>> jobToApplicants;
    public static final String COMPLETE_JOBS = "Complete";

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
                        CircularNode<String> applicants = new CircularNode<String>();
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

    /**
     * Removes the job from the list of incomplete jobs in the
     * database and adds it to the list of complete jobs, in which
     * the user associated with userID is the one who was
     * accepted for the job.
     */
    public static void grantJob(String jobID, String userID) {
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
        DatabaseReference completedRef = firebaseDB.getReference()
                .child(PostJob.JOB_LIST)
                .child(COMPLETE_JOBS)
                .child(jobID);
        DatabaseReference jobRef = firebaseDB.getReference()
                .child(PostJob.JOB_LIST)
                .child(PostJob.INCOMPLETE_JOBS)
                .child(jobID);
        jobRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostedJob job = snapshot.getValue(PostedJob.class);
                CompletedJob completed = new CompletedJob(job, userID);
                completedRef.setValue(completed);
                jobRef.removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EmployerPastJobs", error.getMessage());
            }
        });
    }

    private class PostedJobsAdapter extends JobAdapter {

        public PostedJobsAdapter(@NonNull Context context, @NonNull ArrayList<PostedJob> objects) {
            super(context, R.layout.posted_job_for_employer, R.id.slotJobTitleDescriptor, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View slot = getJobSlot(position, convertView, parent);
            PostedJob job = getItem(position);
            String jobID = job.getJobID();

            Button nextApplicant = slot.findViewById(R.id.nextApplicant);
            nextApplicant.setOnClickListener(onClickGetNextApplicant(slot, jobID));
            Button prevApplicant = slot.findViewById(R.id.previousApplicant);
            prevApplicant.setOnClickListener(onClickGetPrevApplicant(slot, jobID));
            constructApplicant(slot, job.getJobID());

            Button accept = slot.findViewById(R.id.acceptApplicant);
            accept.setOnClickListener(onClickAcceptApplicant(slot, job));
            return slot;
        }

        public View.OnClickListener onClickAcceptApplicant(View slot, PostedJob job) {
            return v -> {
                TextView applicant = slot.findViewById(R.id.userRepDescriptor);
                String applicantID = applicant.getText().toString();
                grantJob(job.getJobID(), applicantID);
                remove(job);
            };
        }

        public void constructApplicant(View slot, String jobID) {
            CircularNode<String> applicants = jobToApplicants.get(jobID);
            if (applicants == null || applicants.isEmpty()) {
                slot.findViewById(R.id.applicantsList).setVisibility(View.GONE);
            }
            TextView applicantID = slot.findViewById(R.id.userRepDescriptor);
            applicantID.setText(applicants.getData());
        }

        public void getNextApplicant(View slot, String jobID) {
            if (!jobToApplicants.containsKey(jobID)) { return; }
            jobToApplicants.replace(jobID, jobToApplicants.get(jobID).getNext());
            constructApplicant(slot, jobID);
        }

        public void getPrevApplicant(View slot, String jobID) {
            if (!jobToApplicants.containsKey(jobID)) { return; }
            jobToApplicants.replace(jobID, jobToApplicants.get(jobID).getPrev());
            constructApplicant(slot, jobID);
        }

        public View.OnClickListener onClickGetNextApplicant(View slot, String jobID) {
            return v -> {
                getNextApplicant(slot, jobID);
            };
        }

        public View.OnClickListener onClickGetPrevApplicant(View slot, String jobID) {
            return v -> {
                getPrevApplicant(slot, jobID);
            };
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
