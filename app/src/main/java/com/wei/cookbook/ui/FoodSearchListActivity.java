package com.wei.cookbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.wei.cookbook.R;
import com.wei.cookbook.model.FoodBean;
import com.wei.cookbook.model.FoodTypeBean;
import com.wei.cookbook.net.FoodPresenter;
import com.wei.cookbook.ui.adapter.BaseAdapter;
import com.wei.cookbook.ui.adapter.BaseViewHolder;

import java.util.List;

import butterknife.Bind;

public class FoodSearchListActivity extends BaseActivity<FoodPresenter> {
    @Bind(R.id.recyclerView_search)
    RecyclerView mRecyclerView_search;

    private BaseAdapter mAdapter_search;
    private FoodTypeBean.ListBean mData_search = null;
   // private String title = getIntent().getStringExtra("title");

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_food_search_list;
    }

    @Override
    protected void setStatusBarColor()
    {

    }

    @Override
    protected FoodPresenter createPresenter()
    {
        return new FoodPresenter(this,this);
    }

    @Override
    protected void initView(Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        mData_search = getIntent().getParcelableExtra(FoodListActivity.class.getSimpleName());
        setLeftBack();
        setTitle(mData_search != null ? mData_search.getName() : "搜索结果");
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setInitialPrefetchItemCount(5);
        mRecyclerView_search.setLayoutManager(manager);
        //关闭动效提升效率
        ((SimpleItemAnimator) mRecyclerView_search.getItemAnimator()).setSupportsChangeAnimations(false);
        //Item高度固定，避免浪费资源
        mRecyclerView_search.setHasFixedSize(true);
        mRecyclerView_search.setItemViewCacheSize(5);

        mAdapter_search = new BaseAdapter<FoodBean>(this, R.layout.item_food_list)
        {
            @Override
            public void convert(BaseViewHolder holder, int position, final FoodBean data)
            {
                List<String> list = data.getAlbums();
                holder.setText(data.getTitle(), R.id.item_title)
                        .setText(data.getImtro(), R.id.item_descript)
                        .setText("步骤  " + data.getSteps().size() + "步", R.id.item_count)
                        .setRoundImageResource((list != null && list.size() != 0) ? list.get(0) : "",
                                R.dimen.dp_5, R.id.item_image)
                        .itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        openActivity(FoodDetailActivity.class, data);
                    }
                });
            }
        };
        mAdapter_search.bindRecyclerView(mRecyclerView_search);
    }


    @Override
    protected void initData()
    {
        super.initData();
        Intent i =getIntent();
        mPresenter.getSearchFoodData(i.getStringExtra("title"));
    }


    @Override
    public void showData(Object o)
    {
        super.showData(o);
        if (o instanceof List)
        {
            List<FoodBean> list = (List<FoodBean>) o;
            mAdapter_search.setData(list);
        }
    }

}
