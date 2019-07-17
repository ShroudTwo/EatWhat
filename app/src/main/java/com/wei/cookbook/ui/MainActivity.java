package com.wei.cookbook.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wei.cookbook.R;
import com.wei.cookbook.model.FoodTypeBean;
import com.wei.cookbook.net.FoodPresenter;
import com.wei.cookbook.ui.adapter.BaseAdapter;
import com.wei.cookbook.ui.adapter.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<FoodPresenter>
{

    @Bind(R.id.left)
    RecyclerView mLabel;
    @Bind(R.id.right)
    RecyclerView mRecyclerView;

    /*错误布局*/
    private ViewStub mError;

    /*适配器*/
    private BaseAdapter mLabelAdapter, mAdapter;

    /*是否正在加载数据*/
    private boolean isLoading = false;



    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_main;
    }

    @Override
    protected void setStatusBarColor()
    {

    }

    @Override
    protected FoodPresenter createPresenter()
    {
        return new FoodPresenter(this, this);
    }


    @Override
    protected void initView(Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        requestPermissions();
        initLabelAdapter();
        initAdapter();
    }


    /*初始化菜谱分类适配器*/
    private void initLabelAdapter()
    {
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setInitialPrefetchItemCount(5);
        mLabel.setLayoutManager(manager);
        //关闭动效提升效率
        ((SimpleItemAnimator) mLabel.getItemAnimator()).setSupportsChangeAnimations(false);
        //Item高度固定，避免浪费资源
        mLabel.setHasFixedSize(true);
        mLabel.setItemViewCacheSize(5);

        mLabelAdapter = new BaseAdapter<FoodTypeBean>(this, R.layout.item_label)
        {
            @Override
            public void convert(BaseViewHolder holder, final int position, final FoodTypeBean data)
            {
                holder.setText(data.getName(), R.id.item_title)
                        .setTextColor(lastPosition == position ? Color.WHITE : getResources().getColor(R.color.textUnSelected), R.id.item_title)
                        .setBackgroundResource(lastPosition == position ? R.drawable.shape_contract_label : 0, R.id.item_title)
                        .itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (lastPosition == position) return;
                        lastPosition = position;
                        notifyDataSetChanged();
                        mAdapter.setData(data.getList());
                    }
                });
            }

            @Override
            protected int setLastPosition()
            {
                return 0;
            }
        };

        mLabelAdapter.bindRecyclerView(mLabel);
    }


    /*初始化适配器*/
    private void initAdapter()
    {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setInitialPrefetchItemCount(5);
        mRecyclerView.setLayoutManager(manager);
        //关闭动效提升效率
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        //Item高度固定，避免浪费资源
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(5);

        mAdapter = new BaseAdapter<FoodTypeBean.ListBean>(this, R.layout.item_right)
        {
            @Override
            public void convert(BaseViewHolder holder, int position, final FoodTypeBean.ListBean data)
            {
                holder.setText(data.getName(), R.id.item_title)
                        .itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        openActivity(FoodListActivity.class, data);
                    }
                });
            }
        };
        mAdapter.bindRecyclerView(mRecyclerView);
    }


    @Override
    protected void initData()
    {
        super.initData();
        mPresenter.getFoodTypeData();
    }

//    @OnClick(R.id.tv_collect)
//    public void onViewClicked()
//    {
//        openActivity(CollectActivity.class, null);
//    }

    @OnClick({R.id.tv_collect, R.id.id_tab_home_img, R.id.id_tab_popular_img, R.id.id_tab_share_img, R.id.id_tab_diy_img, R.id.id_tab_me_img})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_collect://收藏
                openActivity(CollectActivity.class, null);
                break;
            case R.id.id_tab_home_img://首页
                openActivity(MainActivity.class, null);
                break;
            case R.id.id_tab_popular_img://地图
                ImageButton mImageButton1 =findViewById(R.id.id_tab_popular_img);
                mImageButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,LocationActivity.class );
                        startActivity(intent);
                    }
                });
                break;
            case R.id.id_tab_share_img://统计
                ImageButton mImageButton2 =findViewById(R.id.id_tab_share_img);
                mImageButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,Statistics.class );
                        startActivity(intent);
                    }
                });
                break;
            case R.id.id_tab_diy_img://搜索
                ImageButton mImageButton3 =findViewById(R.id.id_tab_diy_img);
                mImageButton3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,SearchActivity.class );
                        startActivity(intent);
                    }
                });
                break;
            case R.id.id_tab_me_img://我的
                openActivity(MeActivity.class, null);
                break;
            default:
                break;
        }
    }


    @Override
    public void showData(Object o)
    {
        super.showData(o);
        if (mError != null)
        {
            mError.setVisibility(View.GONE);
        }
        if (o instanceof List)
        {
            List<FoodTypeBean> list = (List<FoodTypeBean>) o;
            mLabelAdapter.setData((List<FoodTypeBean>) o);
            mAdapter.setData((list != null && list.size() != 0) ? list.get(0).getList() : null);
        }
    }

    @Override
    public void showError()
    {
        super.showError();
        if (mError == null)
        {
            mError = findViewById(R.id.error);
            mError.inflate();
            TextView retry = findViewById(R.id.layout_error).findViewById(R.id.tv);
            retry.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    initData();
                }
            });
        } else
        {
            mError.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 授权
     */
    private void requestPermissions(){

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.INTERNET);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_WIFI_STATE);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CHANGE_WIFI_STATE);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.BLUETOOTH);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CHANGE_NETWORK_STATE);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_CONTACTS);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.BLUETOOTH_ADMIN);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WAKE_LOCK);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.RECEIVE_SMS);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_SMS);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CAMERA);
        }

        if (!permissionList.isEmpty()){  //申请的集合不为空时，表示有需要申请的权限
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),1);
        }else { //所有的权限都已经授权过了

        }
    }

    /**
     * 授权回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0){ //安全写法，如果小于0，肯定会出错了
                    for (int i = 0; i < grantResults.length; i++) {

                        int grantResult = grantResults[i];
                        if (grantResult == PackageManager.PERMISSION_DENIED){ //这个是权限拒绝
                            String s = permissions[i];
                            Toast.makeText(this,s+"权限被拒绝了",Toast.LENGTH_SHORT).show();
                        }else{ //授权成功了
                            //do Something
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

}
