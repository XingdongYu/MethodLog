package com.robog.methodlog;

import android.app.Application;
import com.robog.library.LogConfig;

public class LogApplication extends Application {

    /**
     * 若不配置，默认输出所有方法log
     */
    {

        final LogConfig logConfig = LogConfig.get();
        logConfig.blockClass(LogApplication.class).blockMethod("onCreate");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
