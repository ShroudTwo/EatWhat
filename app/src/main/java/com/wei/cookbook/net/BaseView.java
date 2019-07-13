package com.wei.cookbook.net;


public interface BaseView
{
    /*显示错误信息*/
    void showErrorMsg(String msg);

    /*显示错误页面*/
    void showError();

    /*显示加载页面*/
    void showLoading();

    /*关闭加载页面*/
    void dissLoading();

    /*显示主页面*/
    void showContent();

    /*加载数据*/
    void showData(Object o);
}
