package com.example.quickcashg18;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.EditText;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.AuthProvider;

public class employee_landingInstrumentedTest {

    @Rule
    public ActivityScenarioRule<employee_landing> myRule = new ActivityScenarioRule<>(employee_landing.class);

    @BeforeClass
    public static void setup() { Intents.init(); }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.quickcashg18", appContext.getPackageName());
    }

    @Test
    public void signOut() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.logout2)).perform(click());
        Thread.sleep(1500);
        intended(hasComponent(com.firebase.ui.auth.ui.email.EmailActivity.class.getName()));
    }

    @Test
    public void switchRoleToEmployer() throws InterruptedException {
        onView(withId(R.id.role)).perform(click());
        Thread.sleep(1000);
        intended(hasComponent(employer_landing.class.getName()));
    }
    @Test
    public void switchtoJobsearch() throws InterruptedException {
        onView(withId(R.id.post_job)).perform(click());
        Thread.sleep(1000);
        intended(hasComponent(job_search.class.getName()));
    }

}
