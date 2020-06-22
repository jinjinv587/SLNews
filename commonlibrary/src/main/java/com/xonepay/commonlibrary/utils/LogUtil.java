package com.xonepay.commonlibrary.utils;

import android.util.Log;


/**
 * @author wjq
 * @ClassName: LogUtil
 * @Description: log工具类
 * @date 2017年4月14日19:47:25
 */
public class LogUtil {

    private final static String DEFAULT_TAG = "LogUtil";
    private static final int ENABLE_DEBUG_LEVEL = 5;

    private LogUtil() {
        throw new UnsupportedOperationException("L cannot instantiated!");
    }

    public static void v(String tag, String msg) {
        if (ENABLE_DEBUG_LEVEL >= 5)
            Log.v(tag == null ? DEFAULT_TAG : tag, msg == null ? "" : msg);
    }

    public static void d(String tag, String msg) {
        if (ENABLE_DEBUG_LEVEL >= 4) {
            if (msg.length() > 3000) {
                for (int i = 0; i < msg.length(); i += 3000) {
                    if (i + 3000 < msg.length())
                        Log.d(tag == null ? DEFAULT_TAG : tag, msg == null ? "" : msg.substring(i, i + 3000));
                    else
                        Log.d(tag == null ? DEFAULT_TAG : tag, msg == null ? "" : msg.substring(i, msg.length()));
                }
            } else {
                Log.d(tag == null ? DEFAULT_TAG : tag, msg == null ? "" : msg);
            }

        }

    }

    public static void i(String tag, String msg) {
        if (ENABLE_DEBUG_LEVEL >= 3)
            Log.i(tag == null ? DEFAULT_TAG : tag, msg == null ? "" : msg);
    }

    public static void w(String tag, String msg) {
        if (ENABLE_DEBUG_LEVEL >= 2)
            Log.w(tag == null ? DEFAULT_TAG : tag, msg == null ? "" : msg);
    }

    public static void e(String tag, String msg) {
        if (ENABLE_DEBUG_LEVEL >= 1)
            Log.e(tag == null ? DEFAULT_TAG : tag, msg == null ? "" : msg);
    }

}
