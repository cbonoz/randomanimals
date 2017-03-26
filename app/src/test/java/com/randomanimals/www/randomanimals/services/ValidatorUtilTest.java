package com.randomanimals.www.randomanimals.services;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by cbuonocore on 3/18/17.
 */
public class ValidatorUtilTest {

    final String TOO_SHORT = "us";
    final String TOO_LONG = "usdsafjkasdfjkasjklfsadkfdfasdfksdfjs";

    @Test
    public void testValidateUsernameTrue() throws Exception {
        boolean result = ValidatorUtil.validateUsername("username");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testValidateUsernameFalseLong() throws Exception {
        boolean result = ValidatorUtil.validateUsername(TOO_LONG);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testValidateUsernameFalseShort() throws Exception {
        boolean result = ValidatorUtil.validateUsername(TOO_SHORT);
        Assert.assertEquals(false, result);
    }

}
