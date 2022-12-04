package com.example.quickcashg18;

import androidx.annotation.NonNull;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeePastJobs extends ToolbarActivity {

    private ListView pastJobs;
    private ArrayList<CompletedJob> pastJobsList;
    private FirebaseUser user;
    private DatabaseReference pastJobsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_past_jobs);

        initDatabase();
        initViews();
        refreshPastJobs();
    }

    protected void initDatabase() {
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
        DatabaseReference firebaseDBRef = firebaseDB.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        pastJobsRef = firebaseDBRef.child(PostJob.JOB_LIST)
                .child(FirebaseConstants.COMPLETE_JOBS);
    }

    protected void initViews() {
        pastJobs = findViewById(R.id.pastJobsListEmployee);
    }

    protected void updatePastJobs() {
        CompletedJobsAdapter adapter = new CompletedJobsAdapter(this, pastJobsList);
        pastJobs.setAdapter(adapter);
    }

    protected void refreshPastJobs() {
        pastJobsList = new ArrayList<>();
        pastJobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot jobShot : snapshot.getChildren()) {
                    CompletedJob job = jobShot.getValue(CompletedJob.class);
                    if (job != null && job.getCompleterID().equals(user.getUid())) {
                        pastJobsList.add(job);
                    }
                }
                updatePastJobs();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EmployeePastJobs", error.getMessage());
            }
        });
    }

    private class CompletedJobsAdapter extends ArrayAdapter<CompletedJob> {
        public CompletedJobsAdapter(Context context, List<CompletedJob> objects) {
            super(context, R.layout.completed_job, R.id.slotJobTitleDescriptor, objects);
        }

        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View slot = convertView;
            if (slot == null) {
                slot = LayoutInflater.from(getContext())
                        .inflate(R.layout.completed_job, parent, false);
            }
            TextView jobTitle = slot.findViewById(R.id.slotJobTitleDescriptor);
            TextView totalPay = slot.findViewById(R.id.slotTotalPayDescriptor);
            TextView duration = slot.findViewById(R.id.slotDurationDescriptor);
            TextView urgency = slot.findViewById(R.id.slotUrgencyDescriptor);
            TextView userIDTag = slot.findViewById(R.id.userRepTag);
            TextView userID = slot.findViewById(R.id.userRepDescriptor);
            TextView paidCheck = slot.findViewById(R.id.isPaidDescriptor);
            RatingBar repBar = slot.findViewById(R.id.repBar);

            CompletedJob job = getItem(position);

            jobTitle.setText(job.getJobTitle());
            totalPay.setText("" + job.getTotalPay());
            duration.setText("" + job.getDuration());
            urgency.setText(job.getUrgency());
            userIDTag.setText("Poster ID:  ");
            userID.setText(job.getPosterID());
            paidCheck.setText("" + job.isPaid());

            DatabaseReference ratingRef = pastJobsRef.child(job.getJobID())
                    .child(FirebaseConstants.EMPLOYER_RATING);
            ratingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue(Float.class) != null) {
                        repBar.setRating(snapshot.getValue(Float.class));
                        repBar.setIsIndicator(true);
                    }
                    // User can make a single rating
                    repBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                        Toast.makeText(getContext(), "You gave the employer a " +
                                rating + "-star rating", Toast.LENGTH_LONG)
                                .show();
                        repBar.setIsIndicator(true);
                        FirebaseConstants.employeeRateEmployer(job, rating);
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("EmployeePastJobs", error.getMessage());
                }
            });

            return slot;
        }
    }
}