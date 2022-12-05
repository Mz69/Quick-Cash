package com.example.quickcashg18;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

public class EmployerPastJobs extends ToolbarActivity {

    private static final String FIREBASEDB_URL = "https://quick-cash-g18-default-rtdb.firebaseio.com/";
    private DatabaseReference postedJobsRef;
    private DatabaseReference completedJobsRef;
    private FirebaseUser user;
    private ListView postedJobs;
    private ArrayList<PostedJob> postedJobsList;
    private HashMap<String, CircularNode<String>> jobToApplicants;
    private ListView completedJobs;
    private ArrayList<CompletedJob> completedJobsList;
    private static final String EMPLOYER_PAST_JOBS_LOG = "EmployerPastJobs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_past_jobs);

        //initialize the database instance and creating references for the job details
        initDatabase();
        initViews();
        refreshPostedJobs();
        refreshCompletedJobs();
    }


    protected void initDatabase() {
        //initialize the database and the references relating to the job details
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FIREBASEDB_URL);
        user = FirebaseAuth.getInstance().getCurrentUser();
        postedJobsRef = firebaseDB.getReference(PostJob.JOB_LIST).child(PostJob.INCOMPLETE_JOBS);
        completedJobsRef = firebaseDB.getReference(PostJob.JOB_LIST).child(FirebaseCommon.COMPLETE_JOBS);
    }

    private void initViews() {
        postedJobs = findViewById(R.id.postedJobsList);
        completedJobs = findViewById(R.id.completedJobsList);
    }

    private void updatePostedJobsList() {
        PostedJobsAdapter postedJobsAdapter = new PostedJobsAdapter(this, postedJobsList);
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
                        CircularNode<String> applicants = new CircularNode<>();
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
                Log.e(EMPLOYER_PAST_JOBS_LOG, error.getMessage());
                updatePostedJobsList();
            }
        });
    }

    private void updateCompletedJobs() {
        CompletedJobsAdapter adapter = new CompletedJobsAdapter(this, completedJobsList);
        completedJobs.setAdapter(adapter);
    }

    private void refreshCompletedJobs() {
        completedJobsList = new ArrayList<>();
        completedJobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot jobShot : snapshot.getChildren()) {
                    CompletedJob job = jobShot.getValue(CompletedJob.class);
                    if (job != null && job.getPosterID().equals(user.getUid())) {
                        completedJobsList.add(job);
                    }
                }
                updateCompletedJobs();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(EMPLOYER_PAST_JOBS_LOG, error.getMessage());
            }
        });
    }

    /**
     * Removes the job from the list of incomplete jobs in the
     * database and adds it to the list of complete jobs, in which
     * the user associated with userID is the one who was
     * accepted for the job.
     */
    public static void grantJob(String jobID, String userID) {
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FirebaseCommon.FIREBASE_URL);
        DatabaseReference completedRef = firebaseDB.getReference()
                .child(PostJob.JOB_LIST)
                .child(FirebaseCommon.COMPLETE_JOBS)
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
                Log.e(EMPLOYER_PAST_JOBS_LOG, error.getMessage());
            }
        });
    }

    public static void payUser(String jobID) {
        DatabaseReference firebaseDB = FirebaseDatabase.getInstance(FirebaseCommon.FIREBASE_URL)
                .getReference();
        DatabaseReference jobRef = firebaseDB.child(PostJob.JOB_LIST)
                .child(FirebaseCommon.COMPLETE_JOBS)
                .child(jobID);
        jobRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot jobSnapshot) {
                CompletedJob completed = jobSnapshot.getValue(CompletedJob.class);
                completed.makePayment();
                jobRef.setValue(completed);
                DatabaseReference incomeRef = firebaseDB.child(FirebaseCommon.USER)
                        .child(completed.getCompleterID())
                        .child(FirebaseCommon.USER_INCOME);
                incomeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot incomeSnapshot) {
                        double income = completed.getTotalPay();
                        if (incomeSnapshot.getValue(Double.class) != null) {
                            income += incomeSnapshot.getValue(Double.class);
                        }
                        incomeRef.setValue(income);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(EMPLOYER_PAST_JOBS_LOG, error.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(EMPLOYER_PAST_JOBS_LOG, error.getMessage());
            }
        });
    }

    private class CompletedJobsAdapter extends ArrayAdapter<CompletedJob> {
        public CompletedJobsAdapter(Context context, List<CompletedJob> objects) {
            super(context, R.layout.completed_job_for_employer, R.id.slotJobTitleDescriptor, objects);
        }

        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View slot = convertView;
            if (slot == null) {
                slot = LayoutInflater.from(getContext())
                        .inflate(R.layout.completed_job_for_employer, parent, false);
            }
            TextView jobTitle = slot.findViewById(R.id.slotJobTitleDescriptor);
            TextView totalPay = slot.findViewById(R.id.slotTotalPayDescriptor);
            TextView duration = slot.findViewById(R.id.slotDurationDescriptor);
            TextView urgency = slot.findViewById(R.id.slotUrgencyDescriptor);
            TextView userIDTag = slot.findViewById(R.id.userRepTag);
            TextView userID = slot.findViewById(R.id.userRepDescriptor);
            TextView paidCheck = slot.findViewById(R.id.isPaidDescriptor);
            RatingBar repBar = slot.findViewById(R.id.repBar);
            Button makePayment = slot.findViewById(R.id.makePayment);

            CompletedJob job = getItem(position);

            jobTitle.setText(job.getJobTitle());
            totalPay.setText("" + job.getTotalPay());
            duration.setText("" + job.getDuration());
            urgency.setText(job.getUrgency());
            userIDTag.setText("Completer ID:  ");
            userID.setText(job.getCompleterID());
            paidCheck.setText("" + job.isPaid());

            View.OnClickListener paymentListener = v -> {
                Intent payment = new Intent(EmployerPastJobs.this, PaymentPortal.class);
                payment.putExtra("pay_key", String.valueOf(job.getTotalPay()));
                startActivity(payment);
                payUser(job.getJobID());
                makePayment.setVisibility(View.GONE);
                paidCheck.setText("" + true);
            };
            makePayment.setOnClickListener(paymentListener);

            if (job.isPaid()) {
                makePayment.setVisibility(View.GONE);
            }

            DatabaseReference ratingRef = completedJobsRef.child(job.getJobID())
                    .child(FirebaseCommon.EMPLOYEE_RATING);
            ratingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue(Float.class) != null) {
                        repBar.setRating(snapshot.getValue(Float.class));
                        repBar.setIsIndicator(true);
                    }
                    // User can make a single rating
                    repBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                        Toast.makeText(getContext(), "You gave the employee a " +
                                        rating + "-star rating", Toast.LENGTH_LONG)
                                .show();
                        repBar.setIsIndicator(true);
                        FirebaseCommon.employerRateEmployee(job, rating);
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(EMPLOYER_PAST_JOBS_LOG, error.getMessage());
                }
            });

            return slot;
        }
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
                return;
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
            return v -> getNextApplicant(slot, jobID);
        }

        public View.OnClickListener onClickGetPrevApplicant(View slot, String jobID) {
            return v -> getPrevApplicant(slot, jobID);
        }

    }
}
