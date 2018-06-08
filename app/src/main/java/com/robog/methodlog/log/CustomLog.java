package com.robog.methodlog.log;

import com.orhanobut.logger.Logger;
import com.robog.library.LogDelegate;

public class CustomLog implements LogDelegate {
    @Override
    public void logMethodMessage(String message) {
        Logger.d(message);
    }
}
