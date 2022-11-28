package com.example.quickcashg18;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    Button importPref;

    ArrayList<Job> availableJobs;
    JobAdapter adapter;
    FirebaseDatabase firebaseDB;
    DatabaseReference jobsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);

        initViews();
        initListeners();
        initDatabase();
        refreshJobList();
        initJobList();

        /*
        AdapterView.OnItemClickListener jobSelect = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }*/
    }

    private void initViews() {
        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.list_view);
        importPref = findViewById(R.id.importPreferencesJobSearch);
    }

    private void initListeners() {
        importPref.setOnClickListener(this::onClickImportPreferences);
    }

    private void initDatabase() {
        firebaseDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
        jobsRef = firebaseDB.getReference(PostJob.JOB_LIST).child(PostJob.INCOMPLETE_JOBS);
    }

    private void initJobList() {
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,availableJobs);
        adapter = new JobAdapter(this, availableJobs);
        listView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (hasJob(s)) {
                    adapter.getFilter().filter(s);
                    return true;
                } else {
                    // Search query not found in List View
                    Toast.makeText(JobSearch.this, "Not found", Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                refreshJobList();
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

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

    private boolean hasJob(String desiredJobTitle) {
        for (Job job : availableJobs) {
            if (job.getJobTitle().equalsIgnoreCase(desiredJobTitle)) {
                return true;
            }
        }
        return false;
    }

    public void onClickImportPreferences(View view) {

    }
}