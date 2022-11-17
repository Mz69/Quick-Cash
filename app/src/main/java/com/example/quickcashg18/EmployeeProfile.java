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

/**
 * The EmployeeProfile activity is used by employees to
 * specify their default job preferences for job search.
 */
public class EmployeeProfile extends ToolbarActivity {

    private EditText enterJobTitle;
    private EditText enterMinHourlyWage;
    private EditText enterMinHours;
    private EditText enterMaxHours;
    private EditText enterLocation;
    private Button applyChanges;

    private FirebaseDatabase firebaseDB;
    private DatabaseReference userRef;

    // If the employee's preferences are ever required,
    // these variables should be used to reference them
    // in case the fields are ever renamed in the database.
    public static String PREFERENCES = "EmployeePreferences";
    public static final String JOB_TITLE = "JobTitle";
    public static final String JOB_LOCATION = "Location";
    public static final String MIN_HOURLY_WAGE = "MinHourlyWage";
    public static final String MIN_HOURS = "MinHours";
    public static final String MAX_HOURS = "MaxHours";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        init();
        initDBRef();
        initListeners();
    }

    private void init() {
        enterJobTitle = findViewById(R.id.enterJobTitleEmployee);
        enterLocation = findViewById(R.id.enterJobLocation);
        enterMinHourlyWage = findViewById(R.id.enterMinHourlyWageEmployee);
        enterMinHours = findViewById(R.id.enterMinHoursEmployee);
        enterMaxHours = findViewById(R.id.enterMaxHoursEmployee);
        applyChanges = findViewById(R.id.applyEmployeeProfileChanges);
    }

    private void initDBRef() {
        firebaseDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        userRef = firebaseDB.getReference(FirebaseConstants.USER).child(userId).child(PREFERENCES);
        userRef.child(JOB_TITLE).setValue("");
        userRef.child(JOB_LOCATION).setValue("");
        userRef.child(MIN_HOURLY_WAGE).setValue("");
        userRef.child(MIN_HOURS).setValue("");
        userRef.child(MAX_HOURS).setValue("");
    }

    private void initListeners() {
        applyChanges.setOnClickListener(this::onClickApply);
    }

    private boolean isValidMinHourlyWage() {
        return Validation.isValidDoubleField(enterMinHourlyWage.getText().toString());
    }

    private boolean isValidMinHours() {
        return Validation.isValidDoubleField(enterMinHours.getText().toString());
    }

    private boolean isValidMaxHours() {
        return Validation.isValidDoubleField(enterMaxHours.getText().toString());
    }

    /**
     * Check that all preferences entered by the employee are in the valid formats.
     */
    public boolean isValidProfile() {
        return isValidMinHourlyWage() && isValidMinHours()
                && isValidMaxHours();
    }

    public String getEnteredJobTitle() {
        return enterJobTitle.getText().toString();
    }

    public String getEnteredJobLocation() { return enterLocation.getText().toString();}

    public String getEnteredMinHourlyWage() {
        return enterMinHourlyWage.getText().toString();
    }

    public String getEnteredMinHours() {
        return enterMinHours.getText().toString();
    }

    public String getEnteredMaxHours() {
        return enterMaxHours.getText().toString();
    }

    private void saveProfile() {
        userRef.child(JOB_TITLE)
                .setValue(getEnteredJobTitle());
        userRef.child(MIN_HOURLY_WAGE)
                .setValue(getEnteredMinHourlyWage());
        userRef.child(MIN_HOURS)
                .setValue(getEnteredMinHours());
        userRef.child(MAX_HOURS)
                .setValue(getEnteredMaxHours());
        userRef.child(JOB_LOCATION)
                .setValue(getEnteredJobLocation());
    }

    /**
     * When the employee clicks the button to apply the changes to their
     * preferences, they are saved to their account in the Firebase database.
     */
    public void onClickApply(View view) {
        if (!isValidProfile()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter valid profile fields",
                    Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        saveProfile();
        Toast toast = Toast.makeText(getApplicationContext(), "Your changes have been saved",
                Toast.LENGTH_LONG);
        toast.show();
    }

}