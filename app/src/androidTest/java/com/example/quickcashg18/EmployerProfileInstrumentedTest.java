package com.example.quickcashg18;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.ViewInteraction;
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

public class EmployerProfileInstrumentedTest {

    // Note that we start from SignInActivity for security reasons.
    @Rule
    public ActivityScenarioRule<SignInActivity> myRule = CommonTestFunctions.signInRule();

    /**
     * Since all our espresso tests for EmployerProfile
     * will be simply reading and writing from the backend,
     * we assume the following database structure, with
     * the names of each leaf being replaced with the
     * values of the constants intended.
     * FirebaseConstants.USER
     * |    EmployerProfile.PREFERENCES
     * |    |   EmployerProfile.HOURLY_WAGE
     * |    |   EmployeeProfile.MIN_HOURS
     * |    |   EmployeeProfile.MAX_HOURS
     *
     * We will test writing a valid set of preferences
     * and writing various invalid sets of preferences
     * (e.g. invalid hourly wage, invalid hours, etc),
     * and check the database outputs each time.
     * We will then scrub the database of these inputs,
     * so that the tests can run arbitrarily many times.
     */

    private final String VALID_HOURLY_WAGE = "13.50";
    private final String VALID_MIN_HOURS = "10";
    private final String VALID_MAX_HOURS = "40";

    private final String INVALID_HOURLY_WAGE = "hi";
    private final String INVALID_MIN_HOURS = "43.";
    private final String INVALID_MAX_HOURS = "-1";

    private FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FirebaseConstants.FIREBASE_URL);
    private DatabaseReference userRef;

    private String actualHourlyWage;
    private String actualMinHours;
    private String actualMaxHours;

    @BeforeClass
    public static void setup() {
        Intents.init();
    }

    @AfterClass
    public static void tearDown() { System.gc(); }

    private ViewInteraction getHourlyWageField() {
        return onView(withId(R.id.enterTotalPayEmployer));
    }

    /*private ViewInteraction getMinHoursField() {
        return onView(withId(R.id.enterMinHoursEmployer));
    }*/

    private ViewInteraction getMaxHoursField() {
        return onView(withId(R.id.enterHoursEmployer));
    }

    private ViewInteraction getApplyChangesButton() {
        return onView(withId(R.id.applyEmployerProfileChanges));
    }

    private void enterValidHourlyWage() {
        getHourlyWageField().perform(typeText(VALID_HOURLY_WAGE));
    }

    /*private void enterValidMinHours() {
        getMinHoursField().perform(typeText(VALID_MIN_HOURS));
    }*/

    private void enterValidMaxHours() {
        getMaxHoursField().perform(typeText(VALID_MAX_HOURS));
    }

    private void enterInvalidHourlyWage() {
        getHourlyWageField().perform(typeText(INVALID_HOURLY_WAGE));
    }

    /*private void enterInvalidMinHours() {
        getMinHoursField().perform(typeText(INVALID_MIN_HOURS));
    }*/

    private void enterInvalidMaxHours() {
        getMaxHoursField().perform(typeText(INVALID_MAX_HOURS));
    }

    private void applyChanges() {
        getApplyChangesButton().perform(click());
    }

    /*private void setupQueryTestUserHourlyWage() {
        Query query = userRef.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                actualHourlyWage = snapshot.child(EmployerProfile.TOTAL_PAY).getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EmployerProfileEspressoTest.setupQueryTestUserHourlyWage()", error.getMessage());
            }
        });
    }*/

    /*private void setupQueryTestUserMinHours() {
        Query query = userRef.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                actualMinHours = snapshot.child(EmployerProfile.MIN_HOURS).getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EmployerProfileEspressoTest.setupQueryTestUserMinHours()", error.getMessage());
            }
        });
    }*/

    /*private void setupQueryTestUserMaxHours() {
        Query query = userRef.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                actualMaxHours = snapshot.child(EmployerProfile.MAX_HOURS).getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EmployerProfileEspressoTest.setupQueryTestUserMaxHours()", error.getMessage());
            }
        });
    }*/

    /*private void resetTestUserDB() {
        userRef.child(EmployerProfile.TOTAL_PAY)
                .setValue("");
        userRef.child(EmployerProfile.MIN_HOURS)
                .setValue("");
        userRef.child(EmployerProfile.MAX_HOURS)
                .setValue("");
    }*/

    // Signs in, navigates to the employee preference page,
    // and resets their database configuration.
    /*@Before
    public void resetState() throws InterruptedException {
        CommonTestFunctions.signInValidAccount();
        userRef = firebaseDB.getReference()
                .child(FirebaseConstants.USER)
                .child(CommonTestFunctions.getTestUserID())
                .child(EmployerProfile.PREFERENCES);
        Thread.sleep(3000);
        CommonTestFunctions.confirmLocation();
        Thread.sleep(1500);
        CommonTestFunctions.navigateToEmployerPreferences();
        resetTestUserDB();
        setupQueries();
    }*/

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.quickcashg18", appContext.getPackageName());
    }

    /*public void setupQueries() {
        setupQueryTestUserHourlyWage();
        setupQueryTestUserMinHours();
        setupQueryTestUserMaxHours();
    }*/

    @Test
    public void validHourlyWage() {
        enterValidHourlyWage();
        applyChanges();
        assertEquals(VALID_HOURLY_WAGE, actualHourlyWage);
    }

    /*@Test
    public void validMinHours() {
        enterValidMinHours();
        applyChanges();

        assertEquals(VALID_MIN_HOURS, actualMinHours);
    }*/

    @Test
    public void validMaxHours() {
        enterValidMaxHours();
        applyChanges();

        assertEquals(VALID_MAX_HOURS, actualMaxHours);
    }

    @Test
    public void validEmptyFields() {
        applyChanges();

        assertEquals("", actualHourlyWage);
        assertEquals("", actualMinHours);
        assertEquals("", actualMaxHours);
    }

    @Test
    public void invalidHourlyWage() {
        enterInvalidHourlyWage();
        applyChanges();

        assertEquals("", actualHourlyWage);
    }

    /*@Test
    public void invalidMinHours() {
        enterInvalidMinHours();
        applyChanges();

        assertEquals("", actualMinHours);
    }*/

    @Test
    public void invalidMaxHours() {
        enterInvalidMaxHours();
        applyChanges();

        assertEquals("", actualMaxHours);
    }

}
