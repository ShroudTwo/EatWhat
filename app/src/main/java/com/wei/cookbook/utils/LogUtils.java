package com.wei.cookbook.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;


public class LogUtils
{
    private static boolean isDebug = true;
    private static String TAG = "wei";

    public static void v(String msg)
    {
        if (isDebug)
        {
            Log.v(TAG, msg);
        }
    }

    public static void d(String msg)
    {
        if (isDebug)
        {
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg)
    {
        if (isDebug)
        {
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg)
    {
        if (isDebug)
        {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg)
    {
        if (isDebug)
        {
            Log.e(TAG, msg);
        }
    }


    public static void json(String msg)
    {
        if (isDebug)
        {
            Logger.json(msg);
        }
    }


}
