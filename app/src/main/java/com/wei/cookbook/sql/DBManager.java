package com.wei.cookbook.sql;

import android.content.Context;


import com.wei.cookbook.App;

import org.greenrobot.greendao.database.Database;

/**
 * 功能：数据库操作对象
 */

public class DBManager
{
    private static DBManager mManager;
    private DaoSession mSession;


    private DBManager()
    {

    }

    public static DBManager getManager()
    {
        if (mManager == null)
        {
            synchronized (DBManager.class)
            {
                if (mManager == null)
                {
                    mManager = new DBManager();
                }
            }
        }
        return mManager;
    }

    public DBManager init(Context context)
    {
//        //数据库加密初始化
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, context.getPackageName() + ".db", null);
        Database db = helper.getEncryptedWritableDb(context.getPackageName());
        mSession = new DaoMaster(db).newSession();
        mSession.clear();//清除缓存
        return this;
    }


    public DaoSession getSession()
    {
        if (mSession == null)
        {
            init(App.getContext());
        }
        return mSession;
    }


}
