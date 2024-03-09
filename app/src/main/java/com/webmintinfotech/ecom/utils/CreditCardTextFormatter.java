package com.webmintinfotech.ecom.utils;

import android.text.Editable;
import android.text.TextWatcher;

public class CreditCardTextFormatter implements TextWatcher {
    private String separator = " - ";
    private int divider = 5;

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == null) {
            return;
        }
        String oldString = editable.toString();
        String newString = getNewString(oldString);
        if (!newString.equals(oldString)) {
            editable.replace(0, oldString.length(), newString);
        }
    }

    private String getNewString(String value) {
        String newString = value.replace(separator, "");

        int divider = this.divider;
        while (newString.length() >= divider) {
            newString = newString.substring(0, divider - 1) + this.separator + newString.substring(divider - 1);
            divider += this.divider + separator.length() - 1;
        }
        return newString;
    }
}
