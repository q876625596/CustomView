package com.ly.customview.tools;

import android.util.Log;

/**
 * Created by Administrator on 2016/7/28 0028.
 */

/**
 * Log统一管理类
 */
public class LogUtils {

    private LogUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final String TAG = "LY";

    // 下面四个是默认tag的函数
    public static void i(String msg) {

        if (isDebug) {
            if (msg == null) {
                Log.i(TAG, "msg is null");
            } else {
                Log.i(TAG, msg);
            }
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            if (msg == null) {
                Log.d(TAG, "msg is null");
            } else {
                Log.d(TAG, msg);
            }
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            if (msg == null) {
                Log.e(TAG, "msg is null");
            } else {
                Log.e(TAG, msg);
            }
        }
    }

    public static void v(String msg) {
        if (isDebug) {
            if (msg == null) {
                Log.v(TAG, "msg is null");
            } else {
                Log.v(TAG, msg);
            }
        }
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug) {
            if (msg == null) {
                Log.i(tag, "msg is null");
            } else {
                Log.i(tag, msg);
            }
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            if (msg == null) {
                Log.d(tag, "msg is null");
            } else {
                Log.d(tag, msg);
            }
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            if (msg == null) {
                Log.e(tag, "msg is null");
            } else {
                Log.e(tag, msg);
            }
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            if (msg == null) {
                Log.v(tag, "msg is null");
            } else {
                Log.v(tag, msg);
            }
        }
    }
}
