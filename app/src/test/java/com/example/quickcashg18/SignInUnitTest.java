package com.example.quickcashg18;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * As of writing, there are no methods in SignInActivity other than onCreate.
 * So we do not implement any unit tests.
 */
public class SignInUnitTest {

    static MainActivity mainActivity;

    @BeforeClass
    public static void setup() {
        mainActivity = new MainActivity();
    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }

    @Test
    public void failSignInUnit() {
        fail();
    }

}
