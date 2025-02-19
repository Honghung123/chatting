package com.honghung.chatapp.utils;

import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class DateUtils {
    public static Date addTime(Date date, long amount, ChronoUnit unit) {
        Instant instant = date.toInstant().plus(amount, unit);
        return Date.from(instant);
    }
}
