package com.example.quickcashg18;

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
    private EditText enterUrgency;
    private EditText enterMaxDuration;
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
        enterUrgency = findViewById(R.id.enterUrgencyEmployee);
        enterMinTotalPay = findViewById(R.id.enterMinTotalPayEmployee);
        enterMaxDuration = findViewById(R.id.enterMaxDurationEmployee);
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

    private boolean isValidMaxDuration() {
        return Validation.isValidDoubleField(getEnteredMaxDuration());
    }

    private boolean isValidMaxDistance() {
        return Validation.isValidDoubleField(getEnteredMaxDistance());
    }

    private boolean isValidUrgency() {
        String urgency = getEnteredUrgency();
        return urgency.equals("") || urgency.equals("Urgent") || urgency.equals("Not Urgent");
    }

    /**
     * Check that all preferences entered by the employee are in the valid formats.
     */
    public boolean isValidProfile() {
        return isValidTotalPay() && isValidMaxDuration() && isValidMaxDistance();
    }

    public String getEnteredJobTitle() {
        return enterJobTitle.getText().toString();
    }

    public MyLocation getEnteredJobLocation() { return location; }

    public String getEnteredMinTotalPay() {
        return enterMinTotalPay.getText().toString();
    }

    public String getEnteredUrgency() { return enterUrgency.getText().toString().trim(); }

    public String getEnteredMaxDuration() {
        return enterMaxDuration.getText().toString();
    }

    public String getEnteredMaxDistance() { return enterMaxDistance.getText().toString(); }

    private void saveProfile() {
        EmployeePreferredJob pref = new EmployeePreferredJob(getEnteredJobTitle(), getEnteredMaxDuration(),
                getEnteredMinTotalPay(), getEnteredUrgency(), getEnteredJobLocation(), getEnteredMaxDistance());
        userRef.setValue(pref);
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