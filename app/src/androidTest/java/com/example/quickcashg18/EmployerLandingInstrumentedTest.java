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

public class EmployerLandingInstrumentedTest {

    @Rule
    public ActivityScenarioRule<EmployerLanding> myRule = new ActivityScenarioRule<>(EmployerLanding.class);

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
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                onView(withId(R.id.logout2)).perform(click()));
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                intended(hasComponent(com.firebase.ui.auth.ui.email.EmailActivity.class.getName())));
    }

    @Test
    public void switchRoleToEmployee() throws InterruptedException {
        onView(withId(R.id.role)).perform(click());
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                intended(hasComponent(EmployeeLanding.class.getName())));
    }

}
