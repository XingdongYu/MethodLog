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
     * 类屏蔽列表
     */
    private List<String> mClassBlockList;

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

        mLogMode = LogMode.BLOCK;
        mEnable = true;
    }

    public LogConfig mode(@LogMode.Check int mode) {
        mLogMode = mode;
        return this;
    }

    public int getMode() {
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

        checkClassBlockList();

        for (Class<?> cls : clz) {
            mClassBlockList.add(cls.getName());
        }
        return this;
    }

    public LogConfig blockClass(String... clz) {

        if (mLogMode == LogMode.SELECT) {
            return this;
        }

        checkClassBlockList();

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

    public void clear() {
        MethodLog.sFirst = true;
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

    private void checkClassBlockList() {
        if (mClassBlockList == null) {
            mClassBlockList = new ArrayList<>();
        }
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
