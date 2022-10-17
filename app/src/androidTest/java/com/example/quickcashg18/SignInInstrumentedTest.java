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
        Thread.sleep(1000);
        // Haven't found the ID for the invalid email message
        intended(hasComponent(com.firebase.ui.auth.ui.email.EmailActivity.class.getName()));
    }

    // A password is invalid if it is less than 8 characters
    // or does not contain numbers and letters
    @Test
    public void signUpInvalidPassword() throws InterruptedException {
        onView(withId(com.firebase.ui.auth.R.id.email))
                .perform(typeText("helloworld@dal.ca"))
                .perform(pressImeActionButton());
        Thread.sleep(1000);
        onView(withId(com.firebase.ui.auth.R.id.name))
                .perform(typeText("zzz"));
        onView(withId(com.firebase.ui.auth.R.id.password))
                .perform(typeText("zzzz"));
        onView(withId(com.firebase.ui.auth.R.id.password))
                .perform(pressImeActionButton());
    }

    // To avoid code copy paste in tests relying on sign-in
    private void signInValidAccountCaller() throws InterruptedException {
        onView(withId(com.firebase.ui.auth.R.id.email)).perform(typeText("zc615905@dal.ca"));
        onView(withId(com.firebase.ui.auth.R.id.button_next)).perform(click());
        Thread.sleep(1000);
        onView(withId(com.firebase.ui.auth.R.id.password)).perform(typeText("password"));
        Thread.sleep(500);
        onView(withId(com.firebase.ui.auth.R.id.password)).perform(pressImeActionButton());
        Thread.sleep(1000);
    }

    @Test
    public void signInValidAccount() throws InterruptedException {
        signInValidAccountCaller();
        intended(hasComponent(employee_landing.class.getName()));
    }

    /*@Test
    public void signOut() throws InterruptedException {
        signInValidAccountCaller();
        onView(withId(R.id.logout2)).perform(click());
        Thread.sleep(500);
        //intended(hasComponent(com.firebase.ui.auth.ui.email.EmailActivity.class.getName()), times(2));
    }*/

}
