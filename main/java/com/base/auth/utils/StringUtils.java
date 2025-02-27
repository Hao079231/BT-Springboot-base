package com.base.auth.utils;


import java.util.function.Predicate;
import org.apache.commons.lang.RandomStringUtils;

public class StringUtils {
    public static String generateRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }

    public static String generateUniqueCode(Predicate<String> existsByCode) {
        String code;
        do {
            code = generateRandomString(6);
        } while (existsByCode.test(code));
        return code;
    }
}
