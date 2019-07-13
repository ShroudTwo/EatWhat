package com.wei.cookbook.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wei.cookbook.R;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class SelfSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_setting);
    }

//    public void setHeadStyle()
//    {
//        Glide.with(this).load(R.drawable.head)
//                .apply(bitmapTransform(new BlurTransformation(this, 25), new CenterCrop(this)))
//                .into(blurImageView);
//
//        Glide.with(this).load(R.drawable.head)
//                .bitmapTransform(new CropCircleTransformation(this))
//                .into(avatarImageView);
//    }
}
