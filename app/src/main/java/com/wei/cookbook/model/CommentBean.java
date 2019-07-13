package com.wei.cookbook.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class CommentBean
{

    /**
     * ID : 116886
     * Avartar : http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eqEacia8yO1dR8w1ITudc4JNzhhuOdiaQ8NJ3rAjVXosq1Djx5DlBcPmGmgqwQDibaHiakH3hma0oNOUg/132
     * Nickname : LVLV7502
     * NickName : LVLV7502
     * Content : 非常喜欢这道菜...
     */

    private int ID;
    private String Avartar;
    @Id
    private String Nickname;
    private String NickName;
    private String Content;

    @Generated(hash = 847104246)
    public CommentBean(int ID, String Avartar, String Nickname, String NickName, String Content) {
        this.ID = ID;
        this.Avartar = Avartar;
        this.Nickname = Nickname;
        this.NickName = NickName;
        this.Content = Content;
    }

    @Generated(hash = 373728077)
    public CommentBean() {
    }

    public int getID()
    {
        return ID;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public String getAvartar()
    {
        return Avartar;
    }

    public void setAvartar(String Avartar)
    {
        this.Avartar = Avartar;
    }

    public String getNickname()
    {
        return Nickname;
    }

    public void setNickname(String Nickname)
    {
        this.Nickname = Nickname;
    }

    public String getNickName()
    {
        return NickName;
    }

    public void setNickName(String NickName)
    {
        this.NickName = NickName;
    }

    public String getContent()
    {
        return Content;
    }

    public void setContent(String Content)
    {
        this.Content = Content;
    }
}
