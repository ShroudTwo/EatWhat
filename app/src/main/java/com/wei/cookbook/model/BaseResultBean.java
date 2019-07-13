package com.wei.cookbook.model;

import com.google.gson.annotations.SerializedName;


public class BaseResultBean<T>
{
    @SerializedName("resultcode")
    private int code;
    @SerializedName("reason")
    private String msg;
    @SerializedName("result")
    private DataBean<T> mData;


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

    public DataBean<T> getData()
    {
        return mData;
    }

    public void setData(DataBean<T> data)
    {
        mData = data;
    }

    public static class DataBean<T>
    {
        @SerializedName("data")
        private T t;


        public T getT()
        {
            return t;
        }

        public void setT(T t)
        {
            this.t = t;
        }
    }
}
