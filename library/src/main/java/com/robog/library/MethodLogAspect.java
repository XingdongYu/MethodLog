package com.robog.library;

import android.text.TextUtils;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.List;

@Aspect
public class MethodLogAspect {

    private String mLastTarget = null;

    @Pointcut("execution(* *..*(..))")
    public void logMethod() {

    }

    @Around("logMethod()")
    public void executeMethodLog(ProceedingJoinPoint joinPoint) throws Throwable {

        final LogConfig logConfig = LogConfig.get();

        if (logConfig.isEnable()) {

            final String targetName = String.valueOf(joinPoint.getTarget());

            if (!TextUtils.isEmpty(targetName) && !"null".equals(targetName)) {

                // 获得当前类名
                // 忽略ID
                final String rawName = targetName.split("@")[0];
                // 忽略引用
                final String currentName = rawName.split("[$]")[0];

                // 若为lib中的类，不做处理
                if (currentName.equals(LogConfig.class.getName())
                        || currentName.equals(MethodLog.class.getName())
                        || currentName.equals(getClass().getName())) {

                    joinPoint.proceed();
                    return;
                }

                // 组装方法所在行
                final StringBuilder sbCodeLine = new StringBuilder();
                final StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
                final int lineNumber = stackTrace.getLineNumber();
                final String fileName = stackTrace.getFileName();
                if (lineNumber >= 0) {
                    sbCodeLine.append("(").append(fileName).append(":").append(lineNumber).append(")");
                } else {
                    sbCodeLine.append("(").append(fileName).append(")");
                }

                final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                final int mode = logConfig.getMode();

                // 屏蔽模式
                if (mode == LogMode.BLOCK) {

                    performBlock(targetName, currentName, sbCodeLine.toString(), signature);

                } else {
                    // 选择模式
                    performSelect(targetName, currentName, sbCodeLine.toString(), signature);
                }

            }
        }

        joinPoint.proceed();

    }


    private void performBlock(String targetName, String currentName, String codeLine, MethodSignature signature) {

        final LogConfig logConfig = LogConfig.get();
        final List<String> classBlockList = logConfig.getClassBlockList();
        final List<String> methodBlockList = logConfig.getMethodBlockList();

        boolean blockHit = false;

        // 如果存在屏蔽类
        if (classBlockList != null) {
            for (String blockName : classBlockList) {
                // 命中
                if (targetName.contains(blockName)) {
                    blockHit = true;
                    break;
                }
            }
        }

        // 如果存在屏蔽方法
        if (methodBlockList != null) {
            for (String blockName : methodBlockList) {
                // 命中
                if (signature.getName().startsWith(blockName)) {
                    blockHit = true;
                    break;
                }
            }
        }

        // 若屏蔽没用命中，则输出日志
        if (!blockHit) {

            if (!currentName.equals(mLastTarget)) {
                MethodLog.logClass(targetName);
                MethodLog.logMethod(signature.toLongString() + codeLine);
                mLastTarget = currentName;
            } else {
                MethodLog.logMethod(signature.toLongString() + codeLine);
            }
        }
    }

    private void performSelect(String targetName, String currentName, String codeLine, MethodSignature signature) {

        final LogConfig logConfig = LogConfig.get();
        final List<String> classSelectList = logConfig.getClassSelectList();
        final List<String> methodBlockList = logConfig.getMethodBlockList();

        boolean selectHit = false;

        if (classSelectList != null) {
            for (String selectName : classSelectList) {
                if (targetName.contains(selectName)) {
                    selectHit = true;
                    break;
                }
            }
        }

        // 如果存在屏蔽方法
        if (methodBlockList != null) {
            for (String blockName : methodBlockList) {
                // 命中
                if (signature.getName().startsWith(blockName)) {
                    selectHit = false;
                    break;
                }
            }
        }

        // 若选择命中，则输出日志
        if (selectHit) {

            if (!currentName.equals(mLastTarget)) {
                MethodLog.logClass(targetName);
                MethodLog.logMethod(signature.toLongString() + codeLine);
                mLastTarget = currentName;
            } else {
                MethodLog.logMethod(signature.toLongString() + codeLine);
            }
        }
    }

}
