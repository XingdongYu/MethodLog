package com.robog.library;

import android.text.TextUtils;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.List;

@Aspect
public class MethodLogAspect {

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

                // ID
                final int index = targetName.indexOf("@");
                final String targetId = index > -1 ? targetName.substring(index) : "";

                final StringBuilder stringBuilder = new StringBuilder();
                final String methodName = joinPoint.getSignature().getName();

                final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                String message = null;

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
                        message = stringBuilder
                                .append(signature.toLongString())
                                .append(" [")
                                .append(Thread.currentThread().getName())
                                .append("]")
                                .append(" <")
                                .append(targetId)
                                .append(">")
                                .toString();
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
                        message = stringBuilder
                                .append(signature.toLongString())
                                .append(" [")
                                .append(Thread.currentThread().getName())
                                .append("]")
                                .append(" <")
                                .append(targetId)
                                .append(">")
                                .toString();
                    }
                }

                if (message != null) {
                    logConfig.getLogDelegate().logMethodMessage(message);
                }
            }
        }

        joinPoint.proceed();

    }

}
