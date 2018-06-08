package com.robog.methodlog.log;

import android.support.annotation.NonNull;
import android.util.Log;

import com.orhanobut.logger.LogStrategy;


/**
 * Android Studio 3.1.2同一TAG短时间会合并导致输出错位解决方案
 */
public class LogCatStrategy implements LogStrategy {

    @Override
    public void log(int priority, String tag, @NonNull String message) {
        Log.println(priority, randomKey() + tag, message);
    }

    private int mLast;

    private String randomKey() {
        int random = (int) (10 * Math.random());
        if (random == mLast) {
            random = (random + 1) % 10;
        }
        mLast = random;
        return String.valueOf(random);
    }
}
