package com.example.quickcashg18;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ThemedSpinnerAdapter;

import androidx.annotation.Nullable;

public class JobAdapter2 extends BaseAdapter implements Filterable, ThemedSpinnerAdapter {

    /**
     *
     */
    @Override
    public void setDropDownViewTheme(@Nullable Resources.Theme theme) {

    }

    @Nullable
    @Override
    public Resources.Theme getDropDownViewTheme() {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
