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
 * The EmployerProfile activity is used by employers to
 * specify their default job preferences for job creation.
 */
public class EmployerProfile extends ToolbarActivity {

    private EditText enterJobTitle;
    private EditText enterTotalPay;
    private EditText enterHours;
    private EditText enterUrgency;
    private EditText enterJobDescription;
    private Button selectPreferredLocation;
    private Button applyChanges;
    private Location location;
    private ActivityResultLauncher<Void> getLocation = registerForActivityResult(new LocationResultContract(),
            this::setLocation);
    private DatabaseReference userRef;

    // If the employer's preferences are ever required,
    // these variables should be used to reference them
    // in case the fields are ever renamed in the database.
    public static final String PREFERENCES = "EmployerPreferences";
    public static final String JOB_TITLE = "JobTitle";
    public static final String TOTAL_PAY = "TotalPay";
    public static final String DURATION = "Duration In Hours";
    public static final String LOCATION = "Location";
    public static final String URGENCY = "Urgency";
    public static final String DESCRIPTION = "Description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_profile);

        init();
        initDBRef();
        initListeners();
    }

    private void init() {
        enterJobTitle = findViewById(R.id.enterJobTitleEmployer);
        enterTotalPay = findViewById(R.id.enterHourlyWageEmployer);
        enterHours = findViewById(R.id.enterHoursEmployer);
        enterUrgency = findViewById(R.id.enterUrgencyEmployer);
        enterJobDescription = findViewById(R.id.enterJobDescriptionEmployer);
        selectPreferredLocation = findViewById(R.id.selectPreferredLocationEmployer);
        applyChanges = findViewById(R.id.applyEmployerProfileChanges);
        location = null;
    }

    private void initDBRef() {
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        userRef = firebaseDB.getReference(FirebaseConstants.USER).child(userId).child(PREFERENCES);
    }

    private void initListeners() {
        selectPreferredLocation.setOnClickListener(this::onClickGetLocation);
        applyChanges.setOnClickListener(this::onClickApply);
    }

    private boolean isValidTotalPay() {
        return Validation.isValidDoubleField(enterTotalPay.getText().toString());
    }

    private boolean isValidHours() {
        return Validation.isValidDoubleField(enterHours.getText().toString());
    }

    public boolean isValidUrgency() {
        String urgency = getEnteredUrgency();
        return urgency.equalsIgnoreCase("Urgent") ||
                urgency.equalsIgnoreCase("Not Urgent");
    }

    /**
     * Check that all preferences entered by the employer are in the valid formats.
     */
    public boolean isValidProfile() {
        return isValidTotalPay() && isValidHours() && isValidUrgency();
    }

    private String getEnteredJobTitle() {
        return enterJobTitle.getText().toString().trim();
    }

    private String getEnteredHourlyWage() {
        return enterTotalPay.getText().toString().trim();
    }

    private String getEnteredHours() {
        return enterHours.getText().toString().trim();
    }

    private String getEnteredUrgency() { return enterUrgency.getText().toString(); }

    private String getEnteredJobDescription() {
        return enterJobDescription.getText().toString().trim();
    }

    private Location getEnteredJobLocation() {
        return location;
    }

    private void setLocation(Location l) {
        this.location = l;
    }

    private void saveProfile() {
        userRef.child(JOB_TITLE)
                .setValue(getEnteredJobTitle());
        userRef.child(TOTAL_PAY)
                .setValue(getEnteredHourlyWage());
        userRef.child(DURATION)
                .setValue(getEnteredHours());
        userRef.child(LOCATION)
                .setValue(getEnteredJobLocation());
        userRef.child(URGENCY)
                .setValue(getEnteredUrgency());
        userRef.child(DESCRIPTION)
                .setValue(getEnteredJobDescription());
    }

    public void onClickGetLocation(View view) {
        getLocation.launch(null);
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