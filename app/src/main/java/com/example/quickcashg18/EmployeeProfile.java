package com.example.quickcashg18;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmployeeProfile extends ToolbarActivity {

    // If the employee's preferences are ever required,
    // these variables should be used to reference them
    // in case the fields are ever renamed in the database.
    public static String PREFERENCES = "EmployeePreferences";
    public static final String JOB_TITLE = "JobTitle";
    public static final String MIN_HOURLY_WAGE = "MinHourlyWage";
    public static final String MIN_HOURS = "MinHours";
    public static final String MAX_HOURS = "MaxHours";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

    }

}