package com.wei.cookbook.model;

import com.wei.cookbook.App;
import com.wei.cookbook.sql.UserBeanDao;
import com.wei.cookbook.utils.LogUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.lang.ref.SoftReference;


@Entity
public class UserBean
{
    @Id/*账号*/
    private String account;
    /*密码*/
    @Property
    private String passWord;
    //用户头像
    @Property
    private String uIcon;
    //用户昵称
    @Property
    private String uNickName;

    @Generated(hash = 908363064)
    public UserBean(String account, String passWord, String uIcon, String uNickName) {
        this.account = account;
        this.passWord = passWord;
        this.uIcon = uIcon;
        this.uNickName = uNickName;
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

    public String getuIcon() {
        return uIcon;
    }

    public void setuIcon(String uIcon) {
        this.uIcon = uIcon;
    }

    public String getuNickName() {
        return uNickName;
    }

    public void setuNickName(String uNickName) {
        this.uNickName = uNickName;
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

    public String getUIcon() {
        return this.uIcon;
    }

    public void setUIcon(String uIcon) {
        this.uIcon = uIcon;
    }

    public String getUNickName() {
        return this.uNickName;
    }

    public void setUNickName(String uNickName) {
        this.uNickName = uNickName;
    }
}
