package com.wei.cookbook.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.wei.cookbook.R;
import com.wei.cookbook.config.Config;
import com.wei.cookbook.net.BasePresenter;
import com.wei.cookbook.utils.RxUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


public class SplashActivity extends BaseActivity
{

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_splash;
    }

    @Override
    protected void setStatusBarColor()
    {

    }

    @Override
    protected BasePresenter createPresenter()
    {
        return null;
    }


    @Override
    protected void initView(Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        //关闭导航栏
        final View decorView = getWindow().getDecorView();
        final int uiOption = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOption);
        delay(Config.SPLASH_DELAY_TIME);


    }


    /*延迟启动*/
    private void delay(long time)
    {
        Observable.timer(time, TimeUnit.MILLISECONDS)
                .compose(RxUtils.<Long>rxObservableSchedlers())
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Long>()
                {
                    @Override
                    public void accept(Long aLong) throws Exception
                    {
                        openActivity(LoginActivity.class, null);
                        doFinish(200);
                    }
                });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        getWindow().setBackgroundDrawable(null);
    }
}
