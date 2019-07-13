package com.wei.cookbook.utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class InitializeManager extends IntentService
{
    private static final String TAG = InitializeManager.class.getSimpleName();
    private static final String BUGLY_ID = "2f9c9db8f5";


    public InitializeManager()
    {
        super(TAG);
    }

    public static void init(Context context)
    {
        Intent it = new Intent(context, InitializeManager.class);
        it.setAction(TAG);
        context.startService(it);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action) && action.equals(TAG))
            {
                Logger.addLogAdapter(new AndroidLogAdapter());
                Stetho.initializeWithDefaults(getApplicationContext());
                /*初始化Bugly*/
                initBugly();
            }
        }
    }


    private void initBugly()
    {
        Context context = getApplicationContext();
        String packageName = context.getPackageName();
        String processName = getProcessName(android.os.Process.myPid());
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.initCrashReport(context, BUGLY_ID, true, strategy);
    }

    private String getProcessName(int pid)
    {
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName))
            {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable)
        {
            throwable.printStackTrace();
        } finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            } catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
