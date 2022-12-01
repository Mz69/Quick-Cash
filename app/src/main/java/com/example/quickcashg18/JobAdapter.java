package com.example.quickcashg18;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

/**
 * This class is ArrayAdapter customized for filtering jobs
 * according to the user's preferences.
 *
 * We were originally using ArrayAdapter anyway, but jobs were
 * only filtered by their title, and not by any other preferences.
 * The idea behind customizing ArrayAdapter for our purposes is from this old
 * StackOverflow post at
 * https://stackoverflow.com/questions/6492214/custom-filtering-arrayadapter-in-listview.
 * Most notably, the user Ben's answer was crucial.
 */
public class JobAdapter extends ArrayAdapter<Job> {
    private final static int jobResource = R.layout.listed_job;
    private Filter filter;

    public JobAdapter(Context context, List<Job> jobList) {
        super(context, jobResource, jobList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View slot = convertView;

        if (slot == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            slot = inflater.inflate(jobResource, parent, false);
        }

        TextView title = slot.findViewById(R.id.slotJobTitleDescriptor);
        TextView totalPay = slot.findViewById(R.id.slotTotalPayDescriptor);
        TextView duration = slot.findViewById(R.id.slotDurationDescriptor);
        TextView urgency = slot.findViewById(R.id.slotUrgencyDescriptor);
        TextView distance = slot.findViewById(R.id.slotDistanceDescriptor);

        Job job = getItem(position);

        title.setText(job.getJobTitle());
        totalPay.setText("" + job.getTotalPay());
        duration.setText("" + job.getDuration());
        urgency.setText(job.getUrgency());
        distance.setText("500 km"); // Need to update when sorting by distance works!

        return slot;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new JobFilter();
        }
        return filter;
    }

    private class JobFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            ArrayList<Job> jobs = new ArrayList<>();
            int numJobs = getCount();
            for (int i = 0; i < numJobs; i++) {
                jobs.add(getItem(i));
            }

            if (constraint == null || constraint.length() == 0) {
                results.values = jobs;
                results.count = numJobs;
                return results;
            }

            EmployeePreferredJob prefJob = null;
            try {
                String prefBytes = constraint.toString();
                byte[] data = Base64.getDecoder().decode(prefBytes);
                ByteArrayInputStream inBytes = new ByteArrayInputStream(data);
                ObjectInputStream in = new ObjectInputStream(inBytes);
                prefJob = (EmployeePreferredJob) in.readObject();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(2);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(3);
            }

            if (prefJob == null) {
                return results;
            }
            ArrayList<Job> filteredJobs = new ArrayList<>();
            for (Job j : jobs) {
                if (prefJob.acceptableJob(j)) {
                    filteredJobs.add(j);
                }
            }

            results.values = filteredJobs;
            results.count = filteredJobs.size();
            return results;
        }

        /**
         * Pretty sure this method is destroying
         * the entire original list of jobs!
         * Hence, none are displayed after filtering once
         * and then filtering again.
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (constraint == null || results == null || results.values == null) {
                return;
            }
            clear();
            addAll((List<Job>)results.values);
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
