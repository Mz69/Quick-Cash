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

@RunWith(AndroidJUnit4.class)
public class Employee_Landing_espressoTest {

    public ActivityScenarioRule<employee_landing> myRule = new ActivityScenarioRule<>(employee_landing.class);
   // public IntentsTestRule<employee_landing> myIntentRule = new IntentsTestRule<>(employee_landing.class);

    @BeforeClass
    public static void setup() {
      //  Intents.init();
    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }



}