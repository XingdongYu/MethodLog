package com.robog.library;

import android.util.Log;

final class MethodLog {

    static boolean sFirst = true;
    private static int sLast;

    private static final String TAG = "#MethodLog#";
    private static final String LEFT_TOP = "┌";
    private static final String LEFT_BOTTOM = "└";
    private static final String MIDDLE_SIMBOL = "├";
    private static final String MESSAGE_LINE = "│";
    private static final String DASHED_LINE = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String SOLID_LINE = "───────────────────────────────────────────────────";

    private static final String TOP_LINE = LEFT_TOP + SOLID_LINE + SOLID_LINE;
    private static final String MIDDLE_LINE = MIDDLE_SIMBOL + DASHED_LINE + DASHED_LINE;
    private static final String BOTTOM_LINE = LEFT_BOTTOM + SOLID_LINE + SOLID_LINE;

    static void logClass(String className) {

        checkFirst();

        d(TOP_LINE);
        e(MESSAGE_LINE + className);
        d(MIDDLE_LINE);
    }

    static void logMethod(String message) {
        d(MESSAGE_LINE + message + " [" + Thread.currentThread().getName() + "]");
    }

    private static void checkFirst() {
        if (!sFirst) {
            d(BOTTOM_LINE);
        } else {
            sFirst = false;
        }
    }

    private static void d(String message) {
        Log.d(TAG + randomKey(), message);
    }

    private static void e(String message) {
        Log.e(TAG + randomKey(), message);
    }

    private static String randomKey() {
        int random = (int) (10 * Math.random());
        if (random == sLast) {
            random = (random + 1) % 10;
        }
        sLast = random;
        return String.valueOf(random);
    }

}
