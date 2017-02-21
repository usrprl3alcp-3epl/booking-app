package com.assignment.util;

import java.time.LocalDateTime;

public final class BookingRequestUtils {
    private BookingRequestUtils() {
    }

    public static LocalDateTime beginningOfTheWorkDay() {
        return LocalDateTime.now()
                .withHour(8)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    public static LocalDateTime endOfTheWorkDay() {
        return LocalDateTime.now()
                .withHour(20)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }
}
