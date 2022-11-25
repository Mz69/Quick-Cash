package com.example.quickcashg18;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class JobSearch extends AppCompatActivity {

    SearchView searchView;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    FirebaseDatabase firebaseDB;
    DatabaseReference jobsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);

        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.list_view);

        list = new ArrayList<String>();
        list.add("Walking dog");
        list.add("Job Hunter");
        list.add("Window Washing");
        list.add("Baby sitting");
        list.add("Car cleaner");
        list.add("Movers");
        list.add("Furniture assembler");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (list.contains(s)) {
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
}