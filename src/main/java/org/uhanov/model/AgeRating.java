package org.uhanov.model;

import java.time.LocalDate;

public enum AgeRating {
    EC(3 * DaysInYear.ONE_YEAR_IN_DAYS),
    E(6 * DaysInYear.ONE_YEAR_IN_DAYS),
    E10_PLUS(10 * DaysInYear.ONE_YEAR_IN_DAYS),
    T(13 * DaysInYear.ONE_YEAR_IN_DAYS),
    M(17 * DaysInYear.ONE_YEAR_IN_DAYS),
    AO(18 * DaysInYear.ONE_YEAR_IN_DAYS);

    private final long minimalAge;

    AgeRating(long minimalAge) {
        this.minimalAge = minimalAge;
    }

    public boolean isPassAgeFilter(LocalDate userBirthDate) {
        return LocalDate.now()
                .minusDays(minimalAge)
                .compareTo(userBirthDate) >= 0;
    }

    private interface DaysInYear {
        int ONE_YEAR_IN_DAYS = 365;
    }
}