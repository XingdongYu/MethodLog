package com.robog.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class LogConfig {

    /**
     * 日志触发模式，可选{@link LogMode#BLOCK}和{@link LogMode#SELECT}
     */
    private @LogMode.Check int mLogMode;

    /**
     * 日志是否开启
     */
    private boolean mEnable;

    /**
     * 日志输出策略，可自定义
     */
    private LogDelegate mLogDelegate;

    /**
     * 类屏蔽列表
     */
    private final List<String> mClassBlockList;

    /**
     * 类选择列表
     */
    private List<String> mClassSelectList;

    /**
     * 方法屏蔽列表
     */
    private List<String> mMethodBlockList;

    private static volatile LogConfig INSTANCE = null;

    public static LogConfig get() {
        if (INSTANCE == null) {
            synchronized (LogConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LogConfig();
                }
            }
        }
        return INSTANCE;
    }

    private LogConfig() {
        mClassBlockList = new ArrayList<>();
        // 屏蔽LogConfig
        mClassBlockList.add(LogConfig.class.getName());
        // 屏蔽LogDelegate，避免死循环
        mClassBlockList.add(LogDelegate.class.getName());

        mLogMode = LogMode.BLOCK;
        mEnable = true;
    }

    public LogDelegate getLogDelegate() {
        if (mLogDelegate == null) {
            return LogDelegate.DEFAULT_LOG;
        }
        return mLogDelegate;
    }

    public LogConfig setLogDelegate(LogDelegate logDelegate) {
        this.mLogDelegate = logDelegate;
        return this;
    }

    public LogConfig mode(@LogMode.Check int mode) {
        mLogMode = mode;
        return this;
    }

    public @LogMode.Check int getMode() {
        return mLogMode;
    }

    public LogConfig enable() {
        mEnable = true;
        return this;
    }

    public LogConfig disable() {
        mEnable = false;
        return this;
    }

    public boolean isEnable() {
        return mEnable;
    }

    public LogConfig blockClass(Class<?>... clz) {

        if (mLogMode == LogMode.SELECT) {
            return this;
        }

        for (Class<?> cls : clz) {
            mClassBlockList.add(cls.getName());
        }
        return this;
    }

    public LogConfig blockClass(String... clz) {

        if (mLogMode == LogMode.SELECT) {
            return this;
        }

        mClassBlockList.addAll(Arrays.asList(clz));

        return this;
    }

    public LogConfig selectClass(Class<?>... clz) {

        if (mLogMode == LogMode.BLOCK) {
            return this;
        }

        checkClassSelectList();
        for (Class<?> cls : clz) {
            mClassSelectList.add(cls.getSimpleName());
        }

        return this;
    }

    public LogConfig blockMethod(String... methods) {

        checkMethodBlockList();
        mMethodBlockList.addAll(Arrays.asList(methods));

        return this;
    }

    public List<String> getClassBlockList() {
        return mClassBlockList;
    }

    public List<String> getClassSelectList() {
        return mClassSelectList;
    }

    public List<String> getMethodBlockList() {
        return mMethodBlockList;
    }

    private void checkClassSelectList() {
        if (mClassSelectList == null) {
            mClassSelectList = new ArrayList<>();
        }
    }

    private void checkMethodBlockList() {
        if (mMethodBlockList == null) {
            mMethodBlockList = new ArrayList<>();
        }
    }
}
