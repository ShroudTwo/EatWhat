package com.wei.cookbook.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wei.cookbook.R;
import com.wei.cookbook.net.BasePresenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ImageView imageView_user = (ImageView) findViewById(R.id.imageView_user);
        ImageView imageView_memo = (ImageView)findViewById(R.id.imageView_memo);
        imageView_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeActivity.this,UserProfileActivity.class );
                startActivity(intent);
            }
        });

        imageView_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeActivity.this,FriendsCirActivity.class );
                startActivity(intent);
            }
        });
    }





}


