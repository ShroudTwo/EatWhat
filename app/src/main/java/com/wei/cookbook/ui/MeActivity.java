package com.wei.cookbook.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.wei.cookbook.App;
import com.wei.cookbook.R;
import com.wei.cookbook.config.Config;
import com.wei.cookbook.model.BaseBean;
import com.wei.cookbook.model.CommentBean;
import com.wei.cookbook.net.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;

public class MeActivity extends BaseActivity {

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_me;
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

    protected void initView(Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        setLeftBack();
        setTitle("My Zone");
    }

    @OnClick({R.id.imageView_user, R.id.imageView_memo, R.id.imageView_share, R.id.imageView_setting})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.imageView_user:
                Intent intent_user = new Intent(MeActivity.this,UserProfileActivity.class );
                startActivity(intent_user);
                break;
            case R.id.imageView_memo:
                Intent intent_memo = new Intent(MeActivity.this,FriendsCirActivity.class );
                startActivity(intent_memo);
                break;
            case R.id.imageView_share:

                break;
            case R.id.imageView_setting:
                openActivity(SelfSettingActivity.class, null);
                break;
        }
    }




}


