package com.example.quickcashg18;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This class is a modification of the ArrayAdapter class,
 * sourced from
 * https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/widget/ArrayAdapter.java.
 * It is used in the job search so that users can filter the jobs by preferences (e.g. total pay).
 *
 * Modifying ArrayAdapter instead of overriding ArrayAdapter seemed necessary. We tried
 * to override it and provide the filter ourselves that way, but there were variables in
 * ArrayAdapter that were private and without getters/setters, which we needed in order to make it work.
 *
 * Useless methods from ArrayAdapter, along with its all of its comments, have been removed, so that
 * our own comments are easily distinguished.
 */
public class JobAdapter extends BaseAdapter implements Filterable, ThemedSpinnerAdapter {

    private final Object mLock = new Object();
    private final LayoutInflater mInflater;
    private final Context mContext;

    private int mDropDownResource;

    private List<Job> mObjects;

    private boolean mObjectsFromResources;

    private int mFieldId = 0;

    private boolean mNotifyOnChange = true;

    private ArrayList<Job> mOriginalValues;
    private JobFilter mFilter;

    private LayoutInflater mDropDownInflater;

    public JobAdapter(@NonNull Context context, @LayoutRes int resource,
                      @IdRes int textViewResourceId, @NonNull List<Job> objects) {
        this(context, resource, textViewResourceId, objects, false);
    }

    private JobAdapter(@NonNull Context context, @LayoutRes int resource,
                       @IdRes int textViewResourceId, @NonNull List<Job> objects, boolean objsFromResources) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDropDownResource = resource;
        mObjects = objects;
        mObjectsFromResources = objsFromResources;
        mFieldId = textViewResourceId;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    public @NonNull Context getContext() {
        return mContext;
    }
    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Job getItem(int position) {
        return mObjects.get(position);
    }

    public int getmDropDownResource() { return mDropDownResource; }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Given a job, constructs the layout seen in listed_job.xml.
     * Note that this assumes that mDropDownResource is listed_job.xml,
     * but we have not deleted mDropDownResource. This is because we may
     * want to use this class in the future for something else
     * (e.g. JOB HISTORY), and it is easy to simply extend this class
     * and override getView as desired. For this reason, we have also made
     * a getter method for mDropDownResource (note its use in the inflater here).
     */
    @Override
    public @NonNull View getView(int position, View convertView,
                                 @NonNull ViewGroup parent) {
        View slot = convertView;

        if (slot == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            slot = inflater.inflate(mDropDownResource, parent, false);
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
        distance.setText(""); // Need to update when sorting by distance works!

        return slot;
    }

    private @NonNull View createViewFromResource(@NonNull LayoutInflater inflater, int position,
                                                 View convertView, @NonNull ViewGroup parent, int resource) {
        final View view;
        final TextView text;
        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }
        try {
            if (mFieldId == 0) {
                text = (TextView) view;
            } else {
                text = view.findViewById(mFieldId);
                if (text == null) {
                    throw new RuntimeException("Failed to find view with ID "
                            + mContext.getResources().getResourceName(mFieldId)
                            + " in item layout");
                }
            }
        } catch (ClassCastException e) {
            Log.e("JobAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "JobAdapter requires the resource ID to be a TextView", e);
        }
        final Job item = getItem(position);
        if (item instanceof CharSequence) {
            text.setText((CharSequence) item);
        } else {
            text.setText(item.toString());
        }
        return view;
    }

    @Override
    public void setDropDownViewTheme(Resources.Theme theme) {
        if (theme == null) {
            mDropDownInflater = null;
        } else if (theme == mInflater.getContext().getTheme()) {
            mDropDownInflater = mInflater;
        } else {
            final Context context = new ContextThemeWrapper(mContext, theme);
            mDropDownInflater = LayoutInflater.from(context);
        }
    }
    @Override
    public Resources.Theme getDropDownViewTheme() {
        return mDropDownInflater == null ? null : mDropDownInflater.getContext().getTheme();
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                @NonNull ViewGroup parent) {
        final LayoutInflater inflater = mDropDownInflater == null ? mInflater : mDropDownInflater;
        return createViewFromResource(inflater, position, convertView, parent, mDropDownResource);
    }
    @Override
    public @NonNull Filter getFilter() {
        if (mFilter == null) {
            mFilter = new JobFilter();
        }
        return mFilter;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        final CharSequence[] explicitOptions = super.getAutofillOptions();
        if (explicitOptions != null) {
            return explicitOptions;
        }
        if (!mObjectsFromResources || mObjects == null || mObjects.isEmpty()) {
            return null;
        }
        final int size = mObjects.size();
        final CharSequence[] options = new CharSequence[size];
        mObjects.toArray(options);
        return options;
    }

    /**
     * JobFilter filters jobs using the acceptableJob(Job job) method
     * in EmployeePreferredJob.
     */
    private class JobFilter extends Filter {
        /**
         * Since the given filter methods take in CharSequences to perform their
         * filtering, we assume that the CharSequence represents a String-encoded byte array
         * representing a serialized EmployeePreferredJob object.
         */
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            final FilterResults results = new FilterResults();
            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mObjects);
                }
            }
            if (prefix == null || prefix.length() == 0) {
                final ArrayList<Job> list;
                synchronized (mLock) {
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                // De-serialize the preferred job
                EmployeePreferredJob prefJob = null;
                try {
                    String prefBytes = prefix.toString();
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

                final ArrayList<Job> values;
                synchronized (mLock) {
                    values = new ArrayList<>(mOriginalValues);
                }

                // Filter by the given preferences
                final int count = values.size();
                final ArrayList<Job> newValues = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    final Job value = values.get(i);
                    if (prefJob.acceptableJob(value)) {
                        newValues.add(value);
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mObjects = (List<Job>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}