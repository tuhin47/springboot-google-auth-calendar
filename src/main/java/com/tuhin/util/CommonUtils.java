package com.tuhin.util;

public class CommonUtils {
    public static long getRandomNumber(long min, long max) {
        return (long) ((Math.random() * (max - min)) + min);
    }
}
