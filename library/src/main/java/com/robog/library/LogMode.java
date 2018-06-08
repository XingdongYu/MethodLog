package com.robog.library;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class LogMode {

    public static final int BLOCK = 0;
    public static final int SELECT = 1;

    @IntDef({BLOCK, SELECT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Check {

    }
}
