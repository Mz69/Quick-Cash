package com.example.quickcashg18;

import android.view.View;
import android.widget.EditText;

public class Validation {

    /**
     * This class consists of common validation methods used across the code.
     * For example, we often need to verify that an entered value corresponds to a
     * valid wage in dollars. The isNumeric method will ensure this.
     */

    public static final String NUMERIC = "[0-9](\\.[0-9]{1,2}){0,1}";

    public static boolean isNumeric(EditText view) {
        return view.getText().toString().matches(NUMERIC);
    }

}
