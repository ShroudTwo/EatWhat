package com.wei.cookbook.model;

import com.google.gson.annotations.SerializedName;

public class BaseBean<T>
{
    @SerializedName("resultcode")
    private int code;
    @SerializedName("reason")
    private String msg;
    @SerializedName("result")
    private T t;


    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public T getT()
    {
        return t;
    }

    public void setT(T t)
    {
        this.t = t;
    }
}
