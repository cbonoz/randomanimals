package com.randomanimals.www.randomanimals.services;

import com.randomanimals.www.randomanimals.Constants;

/**
 * Created by cbuonocore on 3/18/17.
 */

public class ValidatorUtil {

    private ValidatorUtil() {

    }

    public static boolean validateUsername(String username) {
        int nameLength = username.length();
        return nameLength >= Constants.MIN_USERNAME_LENGTH &&
                nameLength <= Constants.MAX_USERNAME_LENGTH;
    }
}
