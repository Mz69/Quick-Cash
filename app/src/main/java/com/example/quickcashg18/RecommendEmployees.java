package com.example.quickcashg18;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecommendEmployees extends ToolbarActivity {

    private DatabaseReference userRef;
    private FirebaseUser user;
    private DatabaseReference incompleteJobsRef;
    private ListView recommendedJobs;
    private ArrayList<PostedJob> recommendedJobsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_recommendation);
        initDatabase();
        initViews();
        refreshRecommendedJobs();
    }

    protected void initDatabase() {
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FirebaseCommon.FIREBASE_URL);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = firebaseDB.getReference()
                .child(FirebaseCommon.USER)
                .child(user.getUid());
        incompleteJobsRef = firebaseDB.getReference()
                .child(PostJob.JOB_LIST)
                .child(PostJob.INCOMPLETE_JOBS);
    }

    protected void initViews() {
        recommendedJobs = findViewById(R.id.recommendedJobs);
    }

    protected void refreshRecommendedJobs() {
        recommendedJobsList = new ArrayList<>();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EmployeePreferredJob prefs = snapshot.child(EmployeeProfile.PREFERENCES)
                        .getValue(EmployeePreferredJob.class);
                if (prefs != null) {
                    System.out.println("Not null");
                    incompleteJobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot jobs) {
                            System.out.println("In onDataChange");
                            for (DataSnapshot jobShot : jobs.getChildren()) {
                                System.out.println("In loop");
                                PostedJob job = jobShot.getValue(PostedJob.class);
                                if (prefs.acceptableJob(job)) {
                                    System.out.println("Did it work?");
                                    recommendedJobsList.add(job);
                                }
                            }
                            recommendedJobs.setAdapter(new RecommendedAdapter(
                                    RecommendEmployees.this, R.layout.incomplete_job_for_search,
                                    R.id.slotJobTitleDescriptor, recommendedJobsList));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private class RecommendedAdapter extends JobAdapter {

        protected RecommendedAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<PostedJob> objects) {
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

        public View.OnClickListener getOnClickApplyToJob(PostedJob job) {
            return v -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                JobSearch.applyToJob(job, user.getUid());
                Toast.makeText(getContext(), "Applied successfully", Toast.LENGTH_LONG).show();
            };
        }
    }

}