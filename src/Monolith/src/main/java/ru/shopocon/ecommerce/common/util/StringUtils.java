package ru.shopocon.ecommerce.common.util;

public class StringUtils {
    public static final String NOT_BLANK_PATTERN = "\\A(?!\\s*\\Z).+";

    public static boolean isBlank(String string) {
        return string == null || string.isBlank();
    }

    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }
}
