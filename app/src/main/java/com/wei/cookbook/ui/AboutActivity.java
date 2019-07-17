package com.wei.cookbook.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wei.cookbook.R;
import com.wei.cookbook.net.FoodPresenter;

public class AboutActivity extends BaseActivity {

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_about;
    }

    @Override
    protected void setStatusBarColor()
    {

    }
    @Override
    protected FoodPresenter createPresenter() { return null; }

    @Override
    protected void initView(Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        setTitle("关于");
        setLeftBack();
    }

}
