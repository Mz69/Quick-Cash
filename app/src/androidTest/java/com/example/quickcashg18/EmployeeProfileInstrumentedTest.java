package com.example.quickcashg18;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

import android.content.Context;

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
    @Rule
    public ActivityScenarioRule<SignInActivity> myRule = CommonTestFunctions.signInRule();

    private final String VALID_JOB_TITLE = "Software Engineer";
    private final String VALID_MIN_TOTAL_PAY = "4500.99";
    private final String VALID_MAX_HOURS = "40";

    private final String INVALID_MIN_TOTAL_PAY = "hi";
    private final String INVALID_MAX_HOURS = "-1";

    private FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance(FirebaseCommon.FIREBASE_URL);
    private DatabaseReference userRef;

    private EmployeePreferredJob prefs;

    @BeforeClass
    public static void setup() { Intents.init(); }

    @AfterClass
    public static void tearDown() { System.gc(); }

    private void enterValidJobTitle() {
        onView(withId(R.id.enterJobTitleEmployee)).perform(typeText(VALID_JOB_TITLE));
    }

    private void enterValidMinTotalPay() {
        onView(withId(R.id.enterMinTotalPayEmployee)).perform(typeText(VALID_MIN_TOTAL_PAY));
    }

    private void enterValidMaxHours() {
        onView(withId(R.id.enterMaxDurationEmployee)).perform(typeText(VALID_MAX_HOURS));
    }

    private void enterInvalidMinTotalPay() {
        onView(withId(R.id.enterMinTotalPayEmployee)).perform(typeText(INVALID_MIN_TOTAL_PAY));
    }

    private void enterInvalidMaxHours() {
        onView(withId(R.id.enterMaxDurationEmployee)).perform(typeText(INVALID_MAX_HOURS));
    }

    private void applyChanges() {
        onView(withId(R.id.applyEmployeeProfileChanges)).perform(click());
    }

    private void setupQueryTestUser() {
        Query query = userRef.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prefs = snapshot.getValue(EmployeePreferredJob.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Before
    public void resetState() throws InterruptedException {
        CommonTestFunctions.signInValidAccount();
        userRef = firebaseDB.getReference()
                .child(FirebaseCommon.USER)
                .child(CommonTestFunctions.getTestUserID())
                .child(EmployeeProfile.PREFERENCES);
        Thread.sleep(3000);
        CommonTestFunctions.confirmLocation();
        Thread.sleep(1500);
        CommonTestFunctions.navigateToEmployeePreferences();
        setupQueryTestUser();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.quickcashg18", appContext.getPackageName());
    }

    @Test
    public void validTitle() {
        enterValidJobTitle();
        applyChanges();
        assertEquals(VALID_JOB_TITLE, prefs.getJobTitle());
    }

    @Test
    public void validMinTotalPay() {
        enterValidMinTotalPay();
        applyChanges();
        assertEquals(VALID_MIN_TOTAL_PAY, prefs.getTotalPay());
    }

    @Test
    public void validMaxHours() {
        enterValidMaxHours();
        applyChanges();

        assertEquals(VALID_MAX_HOURS, prefs.getDuration());
    }

    @Test
    public void invalidMinTotalPay() {
        enterInvalidMinTotalPay();
        applyChanges();

        assertEquals(Double.MAX_VALUE, prefs.getTotalPay());
    }

    @Test
    public void invalidMaxHours() {
        enterInvalidMaxHours();
        applyChanges();

        assertEquals(Double.MAX_VALUE, prefs.getDuration());
    }

}
