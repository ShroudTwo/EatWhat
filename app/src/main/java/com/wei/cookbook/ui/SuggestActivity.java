package com.wei.cookbook.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wei.cookbook.R;
import com.wei.cookbook.net.FoodPresenter;
import com.wei.cookbook.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

public class SuggestActivity extends BaseActivity {

    @Bind(R.id.add_photo)
    TextView addPhoto;
    @Bind(R.id.submit_suggest)
    Button submitBtn;
    @Bind(R.id.view_photo)
    ImageView viewPhoto;
    @Bind(R.id.write_suggestion)
    EditText edit_suggestion;


    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_suggest;
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
        setTitle("反馈中心");
        setLeftBack();

    }

    @OnClick({R.id.add_photo, R.id.submit_suggest, R.id.take_photo})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.add_photo:
                choosePhoto();
                break;
            case R.id.submit_suggest:
                Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
                edit_suggestion.setText("");
                viewPhoto.setImageResource(0);
                break;
            case R.id.take_photo:
                takePhoto();
                // 把文件插入到系统图库
                try {
                    MediaStore.Images.Media.insertImage(this.getContentResolver(), mTempPhotoPath, fileName, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 通知图库更新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "/sdcard/namecard/")));
                break;
        }
    }

    //跳转系统相册获取图片
    public static final int RC_CHOOSE_PHOTO = 2;
    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
    }

    // 系统相册获取图片结果返回,相机拍照返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_CHOOSE_PHOTO:
                Uri uri = data.getData();
                String filePath = FileUtil.getFilePathByUri(this, uri);

                if (!TextUtils.isEmpty(filePath)) {
                    RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                    //将照片显示在 ivImage上
                    Glide.with(this).load(filePath).apply(requestOptions1).into(viewPhoto);
                }
                break;
            case RC_TAKE_PHOTO:
                RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                //将图片显示在ivImage上
                Glide.with(this).load(mTempPhotoPath).apply(requestOptions).into(viewPhoto);
                break;
        }
    }

    //拍照选择图片
    public static final int RC_TAKE_PHOTO = 1;

    private String mTempPhotoPath;
    private Uri imageUri;
    String fileName;
    private void takePhoto() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "吃点啥" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        fileName = simpleDateFormat.format(date) + ".png";
        File photoFile = new File(fileDir, fileName);
        mTempPhotoPath = photoFile.getAbsolutePath();
        imageUri = FileProvider.getUriForFile(this, "com.wei.cookbook.fileprovider", photoFile);
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);


    }








}



