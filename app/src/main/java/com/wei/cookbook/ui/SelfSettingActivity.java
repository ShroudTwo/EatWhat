package com.wei.cookbook.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.wei.cookbook.R;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SelfSettingActivity extends AppCompatActivity {
    @Bind(R.id.h_head)
    ImageView mHead;
    @Bind(R.id.h_back)
    ImageView mBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_setting);

    }

    public void setHeadStyle() {
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                .skipMemoryCache(true);//不做内存缓存
        Glide.with(this).load(R.drawable.head)
                .apply(bitmapTransform(new CircleCrop())).into(mHead);

        Glide.with(this).load(R.drawable.head)
                .apply(bitmapTransform(new CropCircleTransformation(this)))
                .into(mHead);
//        Glide.with(this).load(R.drawable.head)
//                .apply(bitmapTransform(new BlurTransformation(this, 25), new CenterCrop(SelfSettingActivity.this)))
//                .into(mBack);
    }

}
