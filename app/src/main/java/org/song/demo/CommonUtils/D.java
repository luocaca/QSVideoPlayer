/***
 This is free and unencumbered software released into the public domain.

 Anyone is free to copy, modify, publish, use, compile, sell, or
 distribute this software, either in source code form or as a compiled
 binary, for any purpose, commercial or non-commercial, and by any
 means.

 For more information, please refer to <http://unlicense.org/>
 */

package org.song.demo.CommonUtils;

/***
 This is free and unencumbered software released into the public domain.
 Anyone is free to copy, modify, publish, use, compile, sell, or
 distribute this software, either in source code form or as a compiled
 binary, for any purpose, commercial or non-commercial, and by any
 means.
 For more information, please refer to <http://unlicense.org/>
 */

import android.util.Log;


/**
 * @author Mr.Zheng
 * @date 2014年10月3日 下午6:23:29
 */
public class D {


    public static final boolean isDebug = true;
    /**
     * Log输出所在类
     */
    private static String className;
    /**
     * Log输出所在方法
     */
    private static String methodName;
    /**
     * Log输出所行号
     */
    private static int lineNumber;

    /**
     * 是否可Debug状态
     *
     * @return
     */
    public static boolean isDebuggable() {
        return true;
    }

    /**
     * 创建Log输出的基本信息
     *
     * @param log
     * @return
     */
    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(methodName);
        buffer.append("()");
        buffer.append(" line:");
        buffer.append(lineNumber);
        buffer.append("] ");
        buffer.append(log);

        return buffer.toString();
    }

    /**
     * 取得输出所在位置的信息 className methodName lineNumber
     *
     * @param sElements
     */
    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName().split("\\.")[0];
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String m) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(m));
    }

    public static void i(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void d(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void v(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void wtf(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }

}
