package com.wei.cookbook;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.wei.cookbook.model.UserBean;
import com.wei.cookbook.sql.DBManager;
import com.wei.cookbook.sql.DaoSession;
import com.wei.cookbook.utils.InitializeManager;


public class App extends Application
{

    private static Context mContext;
    public static DaoSession mSession = null;
    private static UserBean mUser = null;


    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = getApplicationContext();
        mSession = DBManager.getManager().init(this).getSession();
        InitializeManager.init(mContext);
    }


    public static Context getContext()
    {
        return mContext;
    }


    public static UserBean getCacheUser()
    {
        return mUser;
    }

    public static void setUser(UserBean user)
    {
        mUser = user;
    }

}
