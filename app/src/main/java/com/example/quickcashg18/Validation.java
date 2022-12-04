package com.example.quickcashg18;

/**
 * This class consists of common validation methods used across the code.
 * For example, we often need to verify that an entered value corresponds to a
 * valid wage in dollars. The isNumeric method will ensure this.
 */
public class Validation {

    // Created according to guidelines given by SonarQube
    private Validation() { throw new IllegalStateException("Utility class"); }

    public static final String NUMERIC = "\\d+(\\.\\d{1,2})?";

    /**
     * Check if the entered string is a number with up to two decimal places.
     */
    public static boolean isNumeric(String str) {
        return str.matches(NUMERIC);
    }

    /**
     * Check if the entered string is a valid number field with up to two decimal
     * places. The string is allowed to be empty.
     */
    public static boolean isValidDoubleField(String str) {
        return isNumeric(str) || str.equals("");
    }

}
