package com.example.quickcashg18;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import java.time.Duration;

public class EmployeeLandingInstrumentedTest {

    @Rule
    public ActivityScenarioRule<SignInActivity> myRule = new ActivityScenarioRule<>(SignInActivity.class);

    @BeforeClass
    public static void setup() { Intents.init(); }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }

    @Before
    public void resetState() throws InterruptedException {
        CommonTestFunctions.signInValidAccount();
        Thread.sleep(3000);
        CommonTestFunctions.confirmLocation();
        Thread.sleep(1500);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.quickcashg18", appContext.getPackageName());
    }

    @Test
    public void signOut() throws InterruptedException {
                onView(withId(R.id.logout2)).perform(click());
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                intended(hasComponent(com.firebase.ui.auth.ui.email.EmailActivity.class.getName())));
    }

    @Test
    public void switchRoleToEmployer() throws InterruptedException {
        onView(withId(R.id.role)).perform(click());
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                intended(hasComponent(EmployerLanding.class.getName())));
    }
    @Test
    public void switchtoJobsearch() throws InterruptedException {
        onView(withId(R.id.post_job)).perform(click());
        Thread.sleep(1000);
        intended(hasComponent(JobSearch.class.getName()));
    }

}
