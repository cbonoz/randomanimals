package com.randomanimals.www.randomanimals.services;

/**
 * Created by cbuonocore on 3/19/17.
 */

public class HelperUtil {

    private HelperUtil() {

    }

    public static String getCountStringFromCount(int count) {
        final String countString;
        if (count > 1) {
            countString = count + " times";
        } else {
            countString = count + " time";
        }
        return countString;
    }
}
