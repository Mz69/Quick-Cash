package com.example.quickcashg18;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LocationResultContract extends ActivityResultContract<Void, MyLocation> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Void unused) {
        return new Intent(context, MapsActivity.class);
    }

    @Override
    public MyLocation parseResult(int resultCode, @Nullable Intent result) {
        if (resultCode != Activity.RESULT_OK || result == null) {
            return null;
        }
        return new MyLocation((Location)result.getParcelableExtra(MapsActivity.LOCATION_TAG_RESULT));
    }
}
