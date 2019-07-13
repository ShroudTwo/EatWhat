package com.wei.cookbook.net;


import com.wei.cookbook.utils.LogUtils;

import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;


public abstract class BaseObserver<T> extends ResourceSubscriber<T>
{
    private BaseView mView;
    private boolean isShowStateView = false;


    public BaseObserver(BaseView view)
    {
        mView = view;
    }


    public BaseObserver(BaseView view, boolean isShowStateView)
    {
        mView = view;
        this.isShowStateView = isShowStateView;
    }


    @Override
    public void onError(Throwable e)
    {
        String errorMsg = e.getMessage();
        LogUtils.e(errorMsg);
        /*服务器返回的错误*/
        if (e instanceof ApiException)
        {
            switch (((ApiException) e).getCode())
            {
                case ApiException.NO_LOGIN://未登录
                    errorMsg = String.valueOf(ApiException.NO_LOGIN);
                    break;
                default:
                    errorMsg = e.getMessage();
                    break;
            }
        }

        if (e instanceof HttpException)
        {
//            errorMsg = "数据加载失败ヽ(≧Д≦)ノ";
            errorMsg = "Please Check Your Net!";
        }

        if (e instanceof UnknownError)
        {
//            errorMsg = "未知错误ヽ(≧Д≦)ノ";
            errorMsg = "UnknownError";
        }

        if (isShowStateView)
        {
            mView.showError();
        }
        mView.showErrorMsg(errorMsg);
    }


    @Override
    public void onComplete()
    {

    }
}
