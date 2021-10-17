package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;

public final class DateUtil {
    public DateUtil() {
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.toString().replace("T", " ");
    }
}
