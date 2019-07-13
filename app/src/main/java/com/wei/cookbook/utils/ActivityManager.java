package com.wei.cookbook.utils;

import android.app.Activity;
import android.content.Context;

import java.util.List;
import java.util.Stack;


public class ActivityManager
{
    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager()
    {
    }

    /**
     * 单一实例
     */
    public static ActivityManager getManager()
    {
        if (instance == null)
        {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity)
    {
        if (activityStack == null)
        {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity()
    {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity()
    {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity)
    {
        if (activityStack != null && activity != null)
        {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls)
    {
        for (int i = 0; i < activityStack.size(); i++)
        {
            if (activityStack.get(i).getClass().equals(cls))
            {
                finishActivity(activityStack.get(i));
            }
        }
    }

    /**
     * 结束指定多个类名的Activity
     */
    public void finishActivity(List<Class<?>> classs)
    {
        if (classs != null && classs.size() > 0)
        {
            for (Activity activity : activityStack)
            {
                for (int i = 0; i < classs.size(); i++)
                {
                    if (activity.getClass().equals(classs.get(i)))
                    {
                        finishActivity(activity);
                    }
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity()
    {
        for (int i = 0, size = activityStack.size(); i < size; i++)
        {
            if (null != activityStack.get(i))
            {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void exit(Context context)
    {
        try
        {
            finishAllActivity();
            android.app.ActivityManager manager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            manager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e)
        {
        }
    }

}
