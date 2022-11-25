package com.example.quickcashg18;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EmployeeProfileInstrumentedTest {

    // Note that we start from SignInActivity for security reasons.
    @Rule
    public ActivityScenarioRule<SignInActivity> myRule = CommonTestFunctions.signInRule();

    /**
     * Since all our espresso tests for EmployeeProfile
     * will be simply reading and writing from the backend,
     * we assume the following database structure, with
     * the names of each leaf being replaced with the
     * values of the constants intended.
     * FirebaseConstants.USER
     * |    EmployeeProfile.PREFERENCES
     * |    |   EmployeeProfile.JOB_TITLE
     * |    |   EmployeeProfile.MIN_HOURLY_WAGE
     * |    |   EmployeeProfile.MIN_HOURS
     *
     * We will test writing a valid set of preferences
     * and writing various invalid sets of preferences
     * (e.g. invalid hourly wage, invalid hours, etc),
     * and check the database outputs each time.
     * We will then scrub the database of these inputs,
     * so that the tests can run arbitrarily many times.
     */

    private final String VALID_JOB_TITLE = "Software Engineer";
    private final String VALID_MIN_HOURLY_WAGE = "13.50";
    private final String VALID_MIN_HOURS = "10";
    private final String VALID_MAX_HOURS = "40";

    private final String INVALID_MIN_HOURLY_WAGE = "hi";
    private final String INVALID_MIN_HOURS = "43.";
    private final String INVALID_MAX_HOURS = "-1";

    private FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
    private DatabaseReference userRef; /*= firebaseDB.getReference()
            .child(FirebaseConstants.USER)
            .child(CommonTestFunctions.getTestUserID())
            .child(EmployeeProfile.PREFERENCES);*/

    private String actualJobTitle;
    private String actualMinHourlyWage;
    private String actualMinHours;
    private String actualMaxHours;

    @BeforeClass
    public static void setup() {
        Intents.init();
    }

    @AfterClass
    public static void tearDown() { System.gc(); }

    private void enterValidJobTitle() {
        onView(withId(R.id.enterJobTitleEmployee)).perform(typeText(VALID_JOB_TITLE));
    }

    private void enterValidMinHourlyWage() {
        onView(withId(R.id.enterMinTotalPayEmployee)).perform(typeText(VALID_MIN_HOURLY_WAGE));
    }

    private void enterValidMinHours() {
        onView(withId(R.id.enterMinHoursEmployee)).perform(typeText(VALID_MIN_HOURS));
    }

    private void enterValidMaxHours() {
        onView(withId(R.id.enterMaxHoursEmployee)).perform(typeText(VALID_MAX_HOURS));
    }

    private void enterInvalidMinHourlyWage() {
        onView(withId(R.id.enterMinTotalPayEmployee)).perform(typeText(INVALID_MIN_HOURLY_WAGE));
    }

    private void enterInvalidMinHours() {
        onView(withId(R.id.enterMinHoursEmployee)).perform(typeText(INVALID_MIN_HOURS));
    }

    private void enterInvalidMaxHours() {
        onView(withId(R.id.enterMaxHoursEmployee)).perform(typeText(INVALID_MAX_HOURS));
    }

    private void applyChanges() {
        onView(withId(R.id.applyEmployeeProfileChanges)).perform(click());
    }

    private void setupQueryTestUserJobTitle() {
        Query query = userRef.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                actualJobTitle = snapshot.child(EmployeeProfile.JOB_TITLE).getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EmployeeProfileEspressoTest.setupQueryTestUserJobTitle()", error.getMessage());
            }
        });
    }

    private void setupQueryTestUserMinHourlyWage() {
        Query query = userRef.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                actualMinHourlyWage = snapshot.child(EmployeeProfile.MIN_TOTAL_PAY).getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EmployeeProfileEspressoTest.setupQueryTestUserMinHourlyWage()", error.getMessage());
            }
        });
    }

    private void setupQueryTestUserMinHours() {
        Query query = userRef.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                actualMinHours = snapshot.child(EmployeeProfile.MIN_HOURS).getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EmployeeProfileEspressoTest.setupQueryTestUserMinHours()", error.getMessage());
            }
        });
    }

    private void setupQueryTestUserMaxHours() {
        Query query = userRef.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                actualMaxHours = snapshot.child(EmployeeProfile.MAX_HOURS).getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EmployeeProfileEspressoTest.setupQueryTestUserMaxHours()", error.getMessage());
            }
        });
    }

    private void resetTestUserDB() {
        userRef.child(EmployeeProfile.JOB_TITLE)
                .setValue("");
        userRef.child(EmployeeProfile.MIN_TOTAL_PAY)
                .setValue("");
        userRef.child(EmployeeProfile.MIN_HOURS)
                .setValue("");
        userRef.child(EmployeeProfile.MAX_HOURS)
                .setValue("");
    }

    // Signs in, navigates to the employee preference page,
    // and resets their database configuration.
    @Before
    public void resetState() throws InterruptedException {
        CommonTestFunctions.signInValidAccount();
        userRef = firebaseDB.getReference()
                .child(FirebaseConstants.USER)
                .child(CommonTestFunctions.getTestUserID())
                .child(EmployeeProfile.PREFERENCES);
        Thread.sleep(3000);
        CommonTestFunctions.confirmLocation();
        Thread.sleep(1500);
        CommonTestFunctions.navigateToEmployeePreferences();
        resetTestUserDB();
        setupQueries();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.quickcashg18", appContext.getPackageName());
    }

    public void setupQueries() {
        setupQueryTestUserJobTitle();
        setupQueryTestUserMinHourlyWage();
        setupQueryTestUserMinHours();
        setupQueryTestUserMaxHours();
    }

    @Test
    public void validTitle() {
        enterValidJobTitle();
        applyChanges();
        assertEquals(VALID_JOB_TITLE, actualJobTitle);
    }

    @Test
    public void validMinHourlyWage() {
        enterValidMinHourlyWage();
        applyChanges();
        assertEquals(VALID_MIN_HOURLY_WAGE, actualMinHourlyWage);
    }

    @Test
    public void validMinHours() {
        enterValidMinHours();
        applyChanges();

        assertEquals(VALID_MIN_HOURS, actualMinHours);
    }

    @Test
    public void validMaxHours() {
        enterValidMaxHours();
        applyChanges();

        assertEquals(VALID_MAX_HOURS, actualMaxHours);
    }

    @Test
    public void validEmptyFields() {
        applyChanges();

        assertEquals("", actualJobTitle);
        assertEquals("", actualMinHourlyWage);
        assertEquals("", actualMinHours);
        assertEquals("", actualMaxHours);
    }

    @Test
    public void invalidMinHourlyWage() {
        enterInvalidMinHourlyWage();
        applyChanges();

        assertEquals("", actualMinHourlyWage);
    }

    @Test
    public void invalidMinHours() {
        enterInvalidMinHours();
        applyChanges();

        assertEquals("", actualMinHours);
    }

    @Test
    public void invalidMaxHours() {
        enterInvalidMaxHours();
        applyChanges();

        assertEquals("", actualMaxHours);
    }

}