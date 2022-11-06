package com.example.quickcashg18;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.awaitility.Awaitility.await;
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
import com.firebase.ui.auth.ui.email.EmailActivity;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.AuthProvider;
import java.time.Duration;

@RunWith(AndroidJUnit4.class)
public class SignInInstrumentedTest {

    @Rule
    // Launch SignInActivity as MainActivity may have saved the
    // previously logged in user, thus we have no access to the sign-in elements.
    public ActivityScenarioRule<SignInActivity> myRule = new ActivityScenarioRule<>(SignInActivity.class);

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
    public void signInInvalidEmail() throws InterruptedException {
        onView(withId(com.firebase.ui.auth.R.id.email)).perform(typeText("abcd"));
        onView(withId(com.firebase.ui.auth.R.id.button_next)).perform(click());
        // Haven't found the ID for the invalid email message
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                intended(hasComponent(com.firebase.ui.auth.ui.email.EmailActivity.class.getName())));
    }

    // A password is invalid if it is less than 8 characters
    // or does not contain numbers and letters
    @Test
    public void signUpInvalidPassword() throws InterruptedException {
        onView(withId(com.firebase.ui.auth.R.id.email))
                .perform(typeText("DONOTREGISTERTHISEMAIL@dal.ca"))
                .perform(pressImeActionButton());
        Thread.sleep(1500);
        onView(withId(com.firebase.ui.auth.R.id.name))
                        .perform(typeText("zzz"));
        onView(withId(com.firebase.ui.auth.R.id.password))
                .perform(typeText("zzzz"));
        onView(withId(com.firebase.ui.auth.R.id.password))
                .perform(pressImeActionButton());
        await().atMost(Duration.ofSeconds(4)).untilAsserted(() ->
                intended(hasComponent(EmailActivity.class.getName())));
    }

    @Test
    public void signInValidAccount() throws InterruptedException {
        CommonTestFunctions.signInValidAccount();
        await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                intended(hasComponent(MapsActivity.class.getName())));
    }

}
