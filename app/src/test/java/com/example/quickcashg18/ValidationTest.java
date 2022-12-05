package com.example.quickcashg18;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidationTest {

    @Test
    public void validDoubleNumeric() {
        assertTrue(Validation.isNumeric("13.50"));
    }

    @Test
    public void invalidWordIsDoubleField() {
        assertFalse(Validation.isNumeric("hi"));
    }

    @Test
    public void validEmptyIsDoubleField() {
        assertFalse(Validation.isNumeric(""));
    }

    @Test
    public void invalidDoubleIsDoubleField() {
        assertFalse(Validation.isNumeric("13."));
    }

    @Test
    public void validIntegerIsDoubleField() {
        assertTrue(Validation.isNumeric("5351"));
    }

    @Test
    public void invalidWordNumeric() {
        assertFalse(Validation.isNumeric("hi"));
    }

    @Test
    public void invalidEmptyNumeric() {
        assertFalse(Validation.isNumeric(""));
    }


    @Test
    public void invalidDoubleNumeric() {
        assertFalse(Validation.isNumeric("13."));
    }

    @Test
    public void validIntegerNumeric() {
        assertTrue(Validation.isNumeric("5351"));
    }

    @Test
    public void validDoubleIsDoubleField() {
        assertTrue(Validation.isNumeric("13.50"));
    }


}
