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
import java.util.List;

public class EmployerPastJobs extends ToolbarActivity {

    private static final String FIREBASEDB_URL = "https://quick-cash-g18-default-rtdb.firebaseio.com/";
    private FirebaseDatabase firebaseDB;
    private DatabaseReference userRef;
    private DatabaseReference postedJobsRef;
    private FirebaseUser user;
    private ListView postedJobs;
    private PostedJobsAdapter postedJobsAdapter;
    private ArrayList<PostedJob> postedJobsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_jobs2);

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
        postedJobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot incompleteJobs) {
                for (DataSnapshot jobShot : incompleteJobs.getChildren()) {
                    PostedJob job = jobShot.getValue(PostedJob.class);
                    if (job != null &&
                            job.getPosterID().equals(user.getUid())) {
                        postedJobsList.add(job);
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
}
