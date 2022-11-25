package com.example.quickcashg18;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JobSearch extends ToolbarActivity {

    SearchView searchView;
    ListView listView;
    ArrayList<Job> availableJobs;
    ArrayAdapter<Job> adapter;
    FirebaseDatabase firebaseDB;
    DatabaseReference jobsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);

        initDatabase();
        refreshJobListAccordingToParameters();

        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.list_view);

        adapter = new ArrayAdapter<Job>(this, android.R.layout.simple_list_item_1,availableJobs);
        listView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (hasJob(s)) {
                    adapter.getFilter().filter(s);
                } else {
                    // Search query not found in List View
                    Toast.makeText(JobSearch.this, "Not found", Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {

                adapter.getFilter().filter(s);

                return false;
            }
        });
    }

    private void initDatabase() {
        firebaseDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
        jobsRef = firebaseDB.getReference(PostJob.JOB_LIST).child(PostJob.INCOMPLETE_JOBS);
    }

    private void refreshJobListAccordingToParameters() {
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

    private boolean hasJob(String desiredJobTitle) {
        for (Job job : availableJobs) {
            if (job.getJobTitle().equalsIgnoreCase(desiredJobTitle)) {
                return true;
            }
        }
        return false;
    }
}