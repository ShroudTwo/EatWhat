package com.wei.cookbook.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.wei.cookbook.App;
import com.wei.cookbook.R;
import com.wei.cookbook.model.UserBean;
import com.wei.cookbook.net.FoodPresenter;
import com.wei.cookbook.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SelfSettingActivity extends BaseActivity {
    @Bind(R.id.h_head)
    ImageView mHead;
    @Bind(R.id.h_back)
    ImageView mBack;
    @Bind(R.id.user_name)
    TextView mUserName;
    @Bind(R.id.user_val)
    TextView mVar;
    @Bind(R.id.view_down)
    ImageView viewDown;
    @Bind(R.id.imageView_cancel)
    TextView mCancel;


    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_self_setting;
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
        setHeadStyle();
        setTitle("个人中心");
        setLeftBack();
    }


    public void setHeadStyle() {

        String uVar;
        String uName;
        String uIcon;
        UserBean user;
        user = App.getCacheUser();
        uVar = user.getAccount();
        uName = user.getuNickName();
        uIcon = user.getuIcon();

        if(!uVar.equals("123"))
        {
                StringBuilder sb = new StringBuilder(uVar);
                sb.replace(3, 7, "****");
                uVar = sb.toString();
        }
        else
        {
            uName = "超级用户";
        }
        mVar.setText(uVar);
        mUserName.setText(uName);
        mHead.setImageBitmap(returnBitMap(uIcon));
        mBack.setImageBitmap(returnBitMap(uIcon));
        mCancel.getBackground().setAlpha(210);

        //头像背景高斯模糊
        Glide.with(SelfSettingActivity.this)
                .load(uIcon)
                .apply(bitmapTransform(new BlurTransformation(24,2)))
                .into(mBack);
        //底部圆角
        Glide.with(SelfSettingActivity.this)
                .load(R.drawable.bback)
                .apply(bitmapTransform(new RoundedCorners(30))
                        .override(300, 300))
                .into(viewDown);

        //头像圆形
        Glide.with(SelfSettingActivity.this)
                .load(uIcon)
                .into(mHead);

//        //注销圆角
//        Glide.with(SelfSettingActivity.this)
//                .load(R.drawable.head)
//                .apply(bitmapTransform(new RoundedCorners(120))
//                        .override(400, 120))
//                .into(mCancel);
    }



    Bitmap bitmap;
    public Bitmap returnBitMap(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;

                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return bitmap;
    }

    @OnClick({R.id.textView_update, R.id.textView_flush, R.id.textView_about, R.id.textView_suggestion, R.id.imageView_cancel})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.textView_update:
                Toast.makeText(this, "已是最新版！", Toast.LENGTH_LONG).show();
                break;
            case R.id.textView_flush:
                showMsgDialog("是否清除所有图片？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(v.getId() == R.id.dialog_action)
                        {
                            if(deletePhotoes())
                            {
                                alert("图片删除成功");
                                disMsgDialog();
                            }

                        }
                        else
                        {
                            disMsgDialog();
                        }
                    }
                });
                break;
            case R.id.textView_about:
                openActivity(AboutActivity.class, null);
                break;
            case R.id.textView_suggestion:
                openActivity(SuggestActivity.class, null);
                break;
            case R.id.imageView_cancel:
                Toast.makeText(this, "注销成功！", Toast.LENGTH_LONG).show();
                openActivity(LoginActivity.class, null);
                break;
        }
    }

    public boolean deletePhotoes()
    {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(!sdCardExist)
        {
            Toast.makeText(this, "没有权限操作!", Toast.LENGTH_LONG).show();
            return false;
        }
        else
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            String dir = sdDir + "/吃点啥/";
            File file = new File(dir);
            if (dir != null && file.exists() && file.isDirectory()) {
                for (File item : file.listFiles()) {
                    item.delete();
                }
            }
            else
            {
                Toast.makeText(this, "目录下没有图片!", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

}
