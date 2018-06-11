package com.robog.library;

import android.text.TextUtils;
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

        // TODO: 2018/6/8 这里为什么不能抽方法?
        final LogConfig logConfig = LogConfig.get();

        if (logConfig.isEnable()) {

            final String targetName = String.valueOf(joinPoint.getTarget());

            if (!TextUtils.isEmpty(targetName) && !"null".equals(targetName)) {

                // 获得当前类名
                // 忽略ID
                final String rawName = targetName.split("@")[0];
                // 忽略引用
                final String currentName = rawName.split("[$]")[0];

                // 组装方法所在行
                final StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
                final int lineNumber = stackTrace.getLineNumber();
                final String fileName = stackTrace.getFileName();
                final StringBuilder sbCodeLine = new StringBuilder();
                if (lineNumber >= 0) {
                    sbCodeLine.append("(").append(fileName).append(":").append(lineNumber).append(")");
                } else {
                    sbCodeLine.append("(").append(fileName).append(")");
                }

                final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                final String methodName = signature.getName();

                final int mode = logConfig.getMode();
                final List<String> methodBlockList = logConfig.getMethodBlockList();

                // 屏蔽模式
                if (mode == LogMode.BLOCK) {

                    final List<String> classBlockList = logConfig.getClassBlockList();

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
                            if (methodName.startsWith(blockName)) {
                                blockHit = true;
                                break;
                            }
                        }
                    }

                    // 若屏蔽没用命中，则输出日志
                    if (!blockHit) {

                        if (!currentName.equals(mLastTarget)) {
                            MethodLog.logClass(targetName);
                            MethodLog.logMethod(signature.toLongString() + sbCodeLine.toString());
                            mLastTarget = currentName;
                        } else {
                            MethodLog.logMethod(signature.toLongString() + sbCodeLine.toString());
                        }
                    }

                } else {
                    // 选择模式
                    final List<String> classSelectList = logConfig.getClassSelectList();
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
                            if (methodName.startsWith(blockName)) {
                                selectHit = false;
                                break;
                            }
                        }
                    }

                    // 若选择命中，则输出日志
                    if (selectHit) {

                        if (!currentName.equals(mLastTarget)) {
                            MethodLog.logClass(targetName);
                            MethodLog.logMethod(signature.toLongString() + sbCodeLine.toString());
                            mLastTarget = currentName;
                        } else {
                            MethodLog.logMethod(signature.toLongString() + sbCodeLine.toString());
                        }
                    }
                }

            }
        }

        joinPoint.proceed();

    }

}
