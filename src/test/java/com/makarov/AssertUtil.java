package com.makarov;

public class AssertUtil {

    static void assertTrue(boolean result, String failedAssertionMessage) {
        if (!result) throw new AssertionError(failedAssertionMessage);
    }
}
