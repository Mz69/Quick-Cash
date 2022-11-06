package com.example.quickcashg18;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class job_search extends AppCompatActivity {

    SearchView searchView;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

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
                    Toast.makeText(job_search.this, "Not found", Toast.LENGTH_LONG).show();
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
}