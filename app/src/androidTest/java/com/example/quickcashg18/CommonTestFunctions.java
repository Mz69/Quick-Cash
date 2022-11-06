package com.example.quickcashg18;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This class consists of a set of common espresso
 * test functions for our app.
 * For example, we will often need to ensure that
 * the user is signed in in order for our tests
 * to even run.
 */
public class CommonTestFunctions {

    protected static final String VALID_EMAIL = "DONOTDELETETHISACCOUNT@dal.ca";
    protected static final String VALID_PASSWORD = "password";

    /**
     * When a test begins, a developer may already be signed into an account on the
     * emulator, which may have particular usage requirements.
     *
     * To localize particular tests to a single account (specified by VALID_EMAIL and VALID_PASSWORD),
     * we must first start the login activity.
     */
    public static ActivityScenarioRule<SignInActivity> signInRule() {
        return new ActivityScenarioRule<>(SignInActivity.class);
    }

    /**
     * Sign in with the test account specified by VALID_EMAIL and VALID_PASSWORD.
     */
    public static void signInValidAccount() throws InterruptedException {
        try {
            onView(withId(com.firebase.ui.auth.R.id.email)).perform(typeText(VALID_EMAIL));
            onView(withId(com.firebase.ui.auth.R.id.button_next)).perform(click());
            Thread.sleep(1000);
            onView(withId(com.firebase.ui.auth.R.id.password)).perform(typeText(VALID_PASSWORD));
            Thread.sleep(500);
            onView(withId(com.firebase.ui.auth.R.id.password)).perform(pressImeActionButton());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Error: Caught InterruptedException. Please refactor test!");
        }
    }

    /**
     * Navigate to the employee preferences page.
     */
    public static void navigateToEmployeePreferences() {
        onView(withId(R.id.profileEmployee)).perform(click());
    }

    /**
     * Navigate to the employer preferences page.
     */
    public static void navigateToEmployerPreferences() {
        onView(withId(R.id.role)).perform(click());
        onView(withId(R.id.profileEmployer)).perform(click());
    }

    private static DatabaseReference getToUserDB() {
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
        DatabaseReference userRef = firebaseDB.getReference()
                .child(FirebaseConstants.USER);
        return userRef;
    }

    /**
     * Return the FirebaseUser instance of the test user.
     */
    protected static FirebaseUser getTestUserLogin() {
        FirebaseUser testUser = FirebaseAuth.getInstance().getCurrentUser();
        return testUser;
    }

    /**
     * Return the unique user ID of the test user.
     */
    protected static String getTestUserID() {
        return getTestUserLogin().getUid();
    }

}
