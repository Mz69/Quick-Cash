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
 * The EmployerProfile activity is used by employers to
 * specify their default job preferences for job creation.
 */
public class EmployerProfile extends ToolbarActivity {

    private EditText enterHourlyWage;
    private EditText enterMinHours;
    private EditText enterMaxHours;
    private Button applyChanges;

    private FirebaseDatabase firebaseDB;
    private DatabaseReference userRef;

    // If the employee's preferences are ever required,
    // these variables should be used to reference them
    // in case the fields are ever renamed in the database.
    public static String PREFERENCES = "EmployerPreferences";
    public static final String HOURLY_WAGE = "HourlyWage";
    public static final String MIN_HOURS = "MinHours";
    public static final String MAX_HOURS = "MaxHours";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_profile);

        init();
        initDBRef();
        initListeners();
    }

    private void init() {
        enterHourlyWage = findViewById(R.id.enterHourlyWageEmployer);
        enterMinHours = findViewById(R.id.enterMinHoursEmployer);
        enterMaxHours = findViewById(R.id.enterMaxHoursEmployer);
        applyChanges = findViewById(R.id.applyEmployerProfileChanges);
    }

    private void initDBRef() {
        firebaseDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        userRef = firebaseDB.getReference(FirebaseConstants.USER).child(userId).child(PREFERENCES);
        userRef.child(HOURLY_WAGE).setValue("");
        userRef.child(MIN_HOURS).setValue("");
        userRef.child(MAX_HOURS).setValue("");
    }

    private void initListeners() {
        applyChanges.setOnClickListener(this::onClickApply);
    }

    private boolean isValidMinHourlyWage() {
        return Validation.isValidDoubleField(enterHourlyWage.getText().toString());
    }

    private boolean isValidMinHours() {
        return Validation.isValidDoubleField(enterMinHours.getText().toString());
    }

    private boolean isValidMaxHours() {
        return Validation.isValidDoubleField(enterMaxHours.getText().toString());
    }

    /**
     * Check that all preferences entered by the employer are in the valid formats.
     */
    public boolean isValidProfile() {
        return isValidMinHourlyWage() && isValidMinHours()
                && isValidMaxHours();
    }

    private String getEnteredHourlyWage() {
        return enterHourlyWage.getText().toString();
    }

    private String getEnteredMinHours() {
        return enterMinHours.getText().toString();
    }

    private String getEnteredMaxHours() {
        return enterMaxHours.getText().toString();
    }

    private void saveProfile() {
        userRef.child(HOURLY_WAGE)
                .setValue(getEnteredHourlyWage());
        userRef.child(MIN_HOURS)
                .setValue(getEnteredMinHours());
        userRef.child(MAX_HOURS)
                .setValue(getEnteredMaxHours());
    }

    /**
     * When an employer clicks the button to apply the changes to their
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