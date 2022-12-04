package com.example.quickcashg18;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

//import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

public class paymentTests {
    @Rule
    public ActivityScenarioRule<EmployerLanding> mActivityScenarioRule = new ActivityScenarioRule<EmployerLanding>(EmployerLanding.class);

    @BeforeClass
    public static void setup() {
        Intents.init();
    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }


    @Test
    public void switchToPaymentPortal() throws InterruptedException {
        onView(withId(R.id.past_jobs_employer)).perform(click());
        onView(withId(R.id.btnPayNow)).perform(click());
        Thread.sleep(1000);
        intended(hasComponent(payment_portal.class.getName()));
    }
    @Test
    public void paymentTest1() throws InterruptedException {
        //switch to past job view
        ViewInteraction EmployerPastJob = onView(allOf(withId(R.id.past_jobs_employer), withText("Past Jobs"), isDisplayed()));
        EmployerPastJob.perform(click());
        //check that top job in array is not payed for
        ViewInteraction PayFalse = onView(allOf(withId(R.id.isPaidDescriptor),withParent(withParent(withId(R.id.isPaidSection))), isDisplayed()));
        PayFalse.check(matches(withText("false")));
        //click the payment button
        ViewInteraction makePayment = onView(allOf(withId(R.id.makePayment), withText("Make Payment"),childAtPosition(withParent(withId(R.id.completedJobsList)), 1), isDisplayed()));
        makePayment.perform(click());
        //enter tip
        ViewInteraction tipping = onView(allOf(withId(R.id.edtTip),childAtPosition(childAtPosition(withId(android.R.id.content), 0), 1), isDisplayed()));
        tipping.perform(replaceText("5"), ViewActions.closeSoftKeyboard());
        ViewInteraction TipAmount = onView(allOf(withId(R.id.edtTip), withText("5"), childAtPosition(childAtPosition(withId(android.R.id.content), 0), 1), isDisplayed()));
        TipAmount.perform(pressImeActionButton());
        //click make payment button
        ViewInteraction paymentButton = onView(allOf(withId(R.id.btnPayNow), withText("Make Payment"), childAtPosition(childAtPosition(withId(android.R.id.content), 0), 2), isDisplayed()));
        paymentButton.perform(click());
        pressBack();
        ViewInteraction PayTrue = onView(allOf(withId(R.id.isPaidDescriptor), withText("true"), withParent(withParent(withId(R.id.isPaidSection))), isDisplayed()));
        PayTrue.check(matches(withText("true")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

