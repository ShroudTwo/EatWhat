package com.wei.cookbook.model;

import com.wei.cookbook.App;
import com.wei.cookbook.sql.UserBeanDao;
import com.wei.cookbook.utils.LogUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


@Entity
public class UserBean
{
    @Id/*账号*/
    private String account;
    /*密码*/
    private String passWord;

    @Generated(hash = 136244742)
    public UserBean(String account, String passWord)
    {
        this.account = account;
        this.passWord = passWord;
    }

    @Generated(hash = 1203313951)
    public UserBean()
    {
    }

    public String getAccount()
    {
        return this.account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getPassWord()
    {
        return this.passWord;
    }

    public void setPassWord(String passWord)
    {
        this.passWord = passWord;
    }


    /*判断当前账户是否cunzai*/
    public boolean verify()
    {
        UserBeanDao dao = App.mSession.getUserBeanDao();
        UserBean user = dao.queryBuilder().where(UserBeanDao.Properties.Account.eq(this.account),
                UserBeanDao.Properties.PassWord.eq(this.passWord))
                .build().unique();
        return user != null;
    }

    /*注册账号*/
    public boolean register()
    {
        UserBeanDao dao = App.mSession.getUserBeanDao();
        long index = dao.insertOrReplace(this);
        LogUtils.e("index:" + index);
        return index > 0;
    }
}
