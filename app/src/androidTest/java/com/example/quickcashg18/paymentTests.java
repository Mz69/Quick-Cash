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

    public class paymentTests {

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
        public void switchToPastJobs() throws InterruptedException {
            onView(withId(R.id.past_jobs_employer)).perform(click());
            await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                    intended(hasComponent(EmployerPastJobs.class.getName())));
        }
        //this test requires a job to be set posted applied to and the employee accepted before it can be run
        //so I have commented it out for the pipeline
        /*@Test
        public void switchtoPaymentportal() throws InterruptedException {
            onView(withId(R.id.past_jobs_employer)).perform(click());
            onView(withId(R.id.makePayment)).perform(click());
            await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                    intended(hasComponent(PaymentPortal.class.getName())));
        }

         */

    }

