package com.example.quickcashg18;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Filter;

import java.util.ArrayList;
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

            ArrayList<Job> jobs = new ArrayList<Job>();
            int numJobs = getCount();
            for (int i = 0; i < numJobs; i++) {
                jobs.add(getItem(i));
            }

            if (constraint == null || constraint.length() == 0) {
                results.values = jobs;
                results.count = numJobs;
                return results;
            }

            /**
             * Introduce a string tokenizer to parse through the constraint
             * and check if the current job matches the preferences.
             *
             * Going to need to make it so that entering nothing in a job search
             * preference field (not just employee preference) sets the preference
             * to FirebaseConstants.NO_PREFERENCE!
             * Do that in JobSearch.
             */
            String prefs = constraint.toString();
            Scanner prefGet = new Scanner(prefs);
            String title = prefGet.nextLine();
            String totalPay = prefGet.nextLine();
            String minHours = prefGet.nextLine();
            String maxHours = prefGet.nextLine();
            String urgency = prefGet.nextLine();
            // add location in here!
            prefGet.close();
            ArrayList<Job> filteredJobs = new ArrayList<>();
            for (Job j : jobs) {
                if (matchesPreferences(j, title, totalPay, minHours, maxHours, urgency, null)) {
                    filteredJobs.add(j);
                }
            }

            results.values = filteredJobs;
            results.count = filteredJobs.size();
            return results;
        }

        protected boolean matchesPreferences(Job job, String title, String totalPay, String minHours,
                String maxHours, String urgency, MyLocation location) {

            boolean titleMatch = true;
            boolean totalPayMatch = true;
            boolean minHoursMatch = true;
            boolean maxHoursMatch = true;
            boolean urgencyMatch = true;
            boolean locationMatch = true;

            if (!title.equals(FirebaseConstants.NO_PREFERENCE)) {
                titleMatch = job.getJobTitle().startsWith(title.toLowerCase());
            }
            if (!totalPay.equals(FirebaseConstants.NO_PREFERENCE)) {
                totalPayMatch = job.getTotalPay() >= Double.parseDouble(totalPay);
            }
            if (!minHours.equals(FirebaseConstants.NO_PREFERENCE)) {
                minHoursMatch = job.getDuration() >= Double.parseDouble(minHours);
            }
            if (!maxHours.equals(FirebaseConstants.NO_PREFERENCE)) {
                maxHoursMatch = job.getDuration() <= Double.parseDouble(maxHours);
            }
            if (!urgency.equals(FirebaseConstants.NO_PREFERENCE)) {
                urgencyMatch = job.getUrgency().equals(urgency);
            }

            return titleMatch && totalPayMatch && minHoursMatch && maxHoursMatch &&
                    urgencyMatch && locationMatch;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List<Job>) results.values);
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
