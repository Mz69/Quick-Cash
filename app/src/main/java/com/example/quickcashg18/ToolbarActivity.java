package com.example.quickcashg18;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ToolbarActivity extends AppCompatActivity {

    /*
    This class defines the toolbar functions to be used in
    other activities.

    To use the toolbar in your activity, make sure your activity extends this Toolbar class.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar);
    }

    public void onClickBack(View view) {
        // End the current activity and return to
        // the previous activity.
        finish();
    }

}