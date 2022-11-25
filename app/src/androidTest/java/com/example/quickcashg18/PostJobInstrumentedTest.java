package com.example.quickcashg18;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.time.Duration;

public class PostJobInstrumentedTest {

    @Rule
    public ActivityScenarioRule<PostJob> myRule = new ActivityScenarioRule<>(PostJob.class);

    @BeforeClass
    public static void setup() {
        Intents.init();
    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }

    //info for testing Toast messages obtained from :https://www.browserstack.com/guide/test-toast-message-using-espresso
    //needed for testing toast message
    private View decorView;
        @Before
        public void setUp(){
            myRule.getScenario().onActivity(new ActivityScenario.ActivityAction<PostJob>(){
                @Override
                public void perform (PostJob activity){
                    decorView= activity.getWindow().getDecorView();
                }
            });
        }


   @Test
   public void useAppContext() {
        // Context of the app under test.
       Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
       assertEquals("com.example.quickcashg18", appContext.getPackageName());
    }

    @Test
    public void postCompleteJob() throws InterruptedException {
        onView(withId(R.id.jobTitle)).perform(typeText("baby-sitting"));
        onView(withId(R.id.location)).perform(typeText("2121 Shirley St."));
        onView(withId(R.id.totalPay)).perform(typeText("12"));
        onView(withId(R.id.timeFrame)).perform(typeText("4 hours "));
        onView(withId(R.id.urgency)).perform(typeText("Not Urgent"));
        closeSoftKeyboard();
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                onView(withId(R.id.JobButton)).perform(click()));
        //currently stuck on Urgency
        intended(hasComponent(EmployerLanding.class.getName()));

    }

    @Test
    public void checkIfJobNameIsEmpty() throws InterruptedException {
        onView(withId(R.id.jobTitle)).perform(typeText(""));
        onView(withId(R.id.location)).perform(typeText("Halifax"));
        onView(withId(R.id.totalPay)).perform(typeText("100"));
        onView(withId(R.id.timeFrame)).perform(typeText("3 days"));
        onView(withId(R.id.urgency)).perform(typeText("Urgent"));
        closeSoftKeyboard();
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                onView(withId(R.id.JobButton)).perform(click()));



    }

    @Test
    public void checkIfLocationIsEmpty() throws InterruptedException {
        onView(withId(R.id.jobTitle)).perform(typeText("Job3"));
        onView(withId(R.id.location)).perform(typeText(""));
        onView(withId(R.id.totalPay)).perform(typeText("100"));
        onView(withId(R.id.timeFrame)).perform(typeText("2 weeks"));
        onView(withId(R.id.urgency)).perform(typeText("Urgent"));
        closeSoftKeyboard();
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                onView(withId(R.id.JobButton)).perform(click()));
    }
    @Test
    public void checkIfSalaryIsEmpty() throws InterruptedException {
        onView(withId(R.id.jobTitle)).perform(typeText("Job4"));
        onView(withId(R.id.location)).perform(typeText("Halifax"));
        onView(withId(R.id.totalPay)).perform(typeText(""));
        onView(withId(R.id.timeFrame)).perform(typeText("2 hours"));
        onView(withId(R.id.urgency)).perform(typeText("Urgent"));
        closeSoftKeyboard();
        //this is here to account for time it take to lower keyboard in slow emulator
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                onView(withId(R.id.JobButton)).perform(click()));
    }

    @Test
    public void checkIfTimeFrameIsEmpty() throws InterruptedException {
        onView(withId(R.id.jobTitle)).perform(typeText("Job5"));
        onView(withId(R.id.location)).perform(typeText("Halifax"));
        onView(withId(R.id.totalPay)).perform(typeText("20"));
        onView(withId(R.id.timeFrame)).perform(typeText(""));
        onView(withId(R.id.urgency)).perform(typeText("Urgent"));
        closeSoftKeyboard();
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                onView(withId(R.id.JobButton)).perform(click()));
    }
    @Test
    public void checkIfSalaryIsANumber() throws InterruptedException {
        onView(withId(R.id.jobTitle)).perform(typeText("Job6"));
        onView(withId(R.id.location)).perform(typeText("Dalhousie Library"));
        onView(withId(R.id.totalPay)).perform(typeText("abc"));
        onView(withId(R.id.timeFrame)).perform(typeText(""));
        onView(withId(R.id.urgency)).perform(typeText("Urgent"));
        closeSoftKeyboard();

        await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                onView(withId(R.id.JobButton)).perform(click()));

    }

    @Test
    public void checkIfUrgencyIsEmpty() throws InterruptedException {
        onView(withId(R.id.jobTitle)).perform(typeText("Job6"));
        onView(withId(R.id.location)).perform(typeText("Halifax"));
        onView(withId(R.id.totalPay)).perform(typeText("100"));
        onView(withId(R.id.timeFrame)).perform(typeText("5 hours"));
        onView(withId(R.id.urgency)).perform(typeText(""));
        closeSoftKeyboard();
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                onView(withId(R.id.JobButton)).perform(click()));




    }
}
