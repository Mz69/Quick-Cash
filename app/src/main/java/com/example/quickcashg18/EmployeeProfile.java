package com.example.quickcashg18;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

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
    private EditText enterMinTotalPay;
    private EditText enterMinHours;
    private EditText enterMaxHours;
    private EditText enterMaxDistance;
    private Button selectedPreferredLocation;
    private Button applyChanges;

    private MyLocation location;
    private ActivityResultLauncher<Void> getLocation = registerForActivityResult(new LocationResultContract(),
            this::setLocation);

    private FirebaseDatabase firebaseDB;
    private DatabaseReference userRef;

    // If the employee's preferences are ever required,
    // these variables should be used to reference them
    // in case the fields are ever renamed in the database.
    public static String PREFERENCES = "EmployeePreferences";
    public static final String JOB_TITLE = "JobTitle";
    public static final String MAX_DISTANCE = "Max Distance in KM";
    public static final String JOB_LOCATION = "Location";
    public static final String MIN_TOTAL_PAY = "MinTotalPay";
    public static final String MIN_HOURS = "MinHours";
    public static final String MAX_HOURS = "MaxHours";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        initDBRef();
        initPageElements();
        initListeners();
    }

    private void initPageElements() {
        enterJobTitle = findViewById(R.id.enterJobTitleEmployee);
        enterMaxDistance = findViewById(R.id.enterMaxDistanceEmployee);
        selectedPreferredLocation = findViewById(R.id.selectPreferredLocationEmployee);
        location = null;
        enterMinTotalPay = findViewById(R.id.enterMinTotalPayEmployee);
        enterMinHours = findViewById(R.id.enterMinHoursEmployee);
        enterMaxHours = findViewById(R.id.enterMaxHoursEmployee);
        enterMaxDistance = findViewById(R.id.enterMaxDistanceEmployee);
        selectedPreferredLocation = findViewById(R.id.selectPreferredLocationEmployee);
        applyChanges = findViewById(R.id.applyEmployeeProfileChanges);
    }

    private void setLocation(MyLocation l) {
        this.location = l;
    }

    private void initDBRef() {
        firebaseDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        userRef = firebaseDB.getReference(FirebaseConstants.USER).child(userId).child(PREFERENCES);
    }

    private void initListeners() {
        applyChanges.setOnClickListener(this::onClickApply);
        selectedPreferredLocation.setOnClickListener(this::onClickGetLocation);
    }

    private boolean isValidTotalPay() {
        return Validation.isValidDoubleField(getEnteredMinTotalPay());
    }

    private boolean isValidMinHours() {
        return Validation.isValidDoubleField(getEnteredMinHours());
    }

    private boolean isValidMaxHours() {
        return Validation.isValidDoubleField(getEnteredMaxHours());
    }

    private boolean isValidMaxDistance() {
        return Validation.isValidDoubleField(getEnteredMaxDistance());
    }

    /**
     * Check that all preferences entered by the employee are in the valid formats.
     */
    public boolean isValidProfile() {
        return isValidTotalPay() && isValidMinHours()
                && isValidMaxHours() && isValidMaxDistance();
    }

    public String getEnteredJobTitle() {
        return enterJobTitle.getText().toString();
    }

    public MyLocation getEnteredJobLocation() { return location; }

    public String getEnteredMinTotalPay() {
        return enterMinTotalPay.getText().toString();
    }

    public String getEnteredMinHours() {
        return enterMinHours.getText().toString();
    }

    public String getEnteredMaxHours() {
        return enterMaxHours.getText().toString();
    }

    public String getEnteredMaxDistance() { return enterMaxDistance.getText().toString(); }

    private void saveProfile() {
        userRef.child(JOB_TITLE)
                .setValue(getEnteredJobTitle());
        userRef.child(MIN_TOTAL_PAY)
                .setValue(getEnteredMinTotalPay());
        userRef.child(MIN_HOURS)
                .setValue(getEnteredMinHours());
        userRef.child(MAX_HOURS)
                .setValue(getEnteredMaxHours());
        userRef.child(JOB_LOCATION)
                .setValue(getEnteredJobLocation());
        userRef.child(MAX_DISTANCE)
                .setValue(getEnteredMaxDistance());
    }

    public void onClickGetLocation(View view) { getLocation.launch(null); }

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