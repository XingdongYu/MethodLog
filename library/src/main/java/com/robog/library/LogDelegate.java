package com.robog.library;

import android.util.Log;

public interface LogDelegate {

    void logMethodMessage(String message);

    LogDelegate DEFAULT_LOG = new LogDelegate() {

        private static final String TAG = "METHOD_LOG";

        @Override
        public void logMethodMessage(String message) {
            Log.d(TAG, message);
        }
    };
}
