package com.robog.methodlog;

import android.app.Application;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.robog.library.LogConfig;
import com.robog.methodlog.log.CustomLog;
import com.robog.methodlog.log.LogCatStrategy;

public class LogApplication extends Application {

    /**
     * 若不配置，默认输出所有方法log
     */
    {

        PrettyFormatStrategy strategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .logStrategy(new LogCatStrategy())
                .methodCount(1)
                .tag("METHOD_LOG")
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(strategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        final LogConfig logConfig = LogConfig.get();
        logConfig.setLogDelegate(new CustomLog())
                // 须屏蔽CustomLog以及LogCatStrategy两个类
                .blockClass(CustomLog.class, LogCatStrategy.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
