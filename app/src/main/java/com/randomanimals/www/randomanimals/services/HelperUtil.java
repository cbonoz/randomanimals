package com.randomanimals.www.randomanimals.services;

import java.text.NumberFormat;
import java.util.Locale;

public class HelperUtil {

    private HelperUtil() {
    }

    private static final NumberFormat numberFormat
            = NumberFormat.getNumberInstance(Locale.getDefault());

    public static String getCountStringFromCount(int count) {
        final String countString;
        String numberAsString = numberFormat.format(count);
        if (count > 1) {
            countString = numberAsString + " points";
        } else {
            countString = numberAsString + " point";
        }
        return countString;
    }

}
