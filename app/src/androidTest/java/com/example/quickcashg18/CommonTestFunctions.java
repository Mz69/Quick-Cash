package com.example.quickcashg18;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.google.firebase.auth.FirebaseAuth;

public class CommonTestFunctions {
    /**
     * This class consists of a set of common espresso
     * test functions for our app.
     * For example, we will often need to ensure that
     * the user is signed in in order for our tests
     * to even run.
     */

    protected static final String VALID_EMAIL = "DONOTDELETETHISACCOUNT@dal.ca";
    protected static final String VALID_PASSWORD = "password";

    public static void signInValidAccount() throws InterruptedException {
        onView(withId(com.firebase.ui.auth.R.id.email)).perform(typeText(VALID_EMAIL));
        onView(withId(com.firebase.ui.auth.R.id.button_next)).perform(click());
        Thread.sleep(1000);
        onView(withId(com.firebase.ui.auth.R.id.password)).perform(typeText(VALID_PASSWORD));
        Thread.sleep(500);
        onView(withId(com.firebase.ui.auth.R.id.password)).perform(pressImeActionButton());
        Thread.sleep(1000);
    }

}
