package com.wei.cookbook.sql;

import android.app.DownloadManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wei.cookbook.model.Profile;
import com.wei.cookbook.model.Speaking;

import org.greenrobot.greendao.query.Query;

import java.util.Date;
import java.util.List;
import java.util.Queue;

public class DBMaster {
    private static DBMaster mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private SpeakingDao mSpeakingDao;
    private ProfileDao mProfileDao;


    public DBMaster(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, "yang.db", null);
        mDaoMaster = new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        //获取dao实例
        mSpeakingDao = mDaoSession.getSpeakingDao();
        mProfileDao = mDaoSession.getProfileDao();
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBMaster getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBMaster.class) {
                if (mInstance == null) {
                    mInstance = new DBMaster(context);
                }
            }
        }
        return mInstance;
    }

    //获取可读数据库
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, "yang.db", null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    //获取可写数据库
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, "yang.db", null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    //插入方法
    public void insertOrReplace(Speaking speaking) {
        mSpeakingDao.insertOrReplace(speaking);
        return;
    }

    public void insertOrReplace(Profile profile){
        mProfileDao.insertOrReplace(profile);
        return;
    }


    //删除方法
    public void delete(Speaking speaking) {
        mSpeakingDao.delete(speaking);
    }

    //显示方法
    public List<Speaking> searchAll() {
        List<Speaking>  mSpeakings = mSpeakingDao.loadAll();
        return mSpeakings;
    }

    public List<Profile> searchAllProfile() {
        List<Profile> mProfile =  mProfileDao.loadAll();
        return  mProfile;
    }

    //修改方法
    public void alterProfile(Profile profile){
        mProfileDao.update(profile);
    }

    public List<Speaking> searchByDate(Date date){
       List <Speaking> speaking = mSpeakingDao.queryBuilder().where(SpeakingDao.Properties.MDate.le(date)).list();
        return speaking;
    }

}