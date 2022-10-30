package com.example.quickcashg18;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import android.content.Context;

//import androidx.test.espresso.intent.Intents;
//import androidx.test.espresso.intent.rule.IntentsTestRule;
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
//import static androidx.test.espresso.intent.Intents.intended;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponet;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class Employee_Landing_espressoTest {
    @Rule
  //  public ActivityScenarioRule<employee_landing> myRule = new ActivityScenarioRule<>(employee_landing.class);
  //  public IntentsTestRule<employee_landing> myIntentRule = new IntentsTestRule<>(employee_landing.class);

    @BeforeClass
    public static void setup() {
  //      Intents.init();
    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }



}
