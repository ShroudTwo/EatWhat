package com.wei.cookbook.ui.adapter;

import android.content.res.Resources;
import android.support.annotation.IntRange;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.wei.cookbook.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDelegateAdapter<T> extends DelegateAdapter.Adapter<BaseViewHolder>
{
    protected BaseActivity mActivity;
    protected Resources mResources;
    private int mResourceLayout;
    private LayoutHelper mHelper;
    protected int lastPosition;
    protected List<T> mList;
    private View mEmptyView = null;

    protected static final int EMPTY = 0x001;//空布局标签

    public BaseDelegateAdapter(BaseActivity activity, int layout)
    {
        this.mActivity = activity;
        this.mResourceLayout = layout;
        this.mResources = mActivity.getApplication().getResources();
        mList = new ArrayList<>();
        lastPosition = setLastPosition();
    }


    public void setData(List<T> list)
    {
        mList.clear();
        mList.addAll(list == null ? new ArrayList<T>() : list);
        notifyDataSetChanged();
    }


    public void setData(T t)
    {
        if (t == null) return;
        mList.clear();
        mList.add(t);
        notifyDataSetChanged();
    }

    public void setData(@IntRange(from = 0) int index, T t)
    {
        if (t == null) return;
        int position = (index < 0 || index >= mList.size()) ? mList.size() : index;
        mList.add(position, t);
        notifyItemInserted(position);
    }

    public void setData(@IntRange(from = 0) int index, List<T> list)
    {
        if (list == null || list.isEmpty()) return;
        int position = (index < 0 || index >= mList.size()) ? mList.size() : index;
        mList.addAll(position, list);
        notifyItemRangeInserted(position, list.size());
    }

    public void clearData()
    {
        if (mList == null || mList.isEmpty()) return;
        mList.clear();
        notifyDataSetChanged();
    }

    public List<T> getData()
    {
        return mList;
    }


    public void setEmptyView(View empty)
    {
        if (empty == null) return;
        this.mEmptyView = empty;
        notifyDataSetChanged();
    }


    public BaseDelegateAdapter setLayoutHelper(LayoutHelper helper)
    {
        this.mHelper = helper;
        return this;
    }

    protected int setLastPosition()
    {
        return -1;
    }

    public void setLastPosition(int position)
    {
        this.lastPosition = position;
        notifyDataSetChanged();
    }


    public int getLastPosition()
    {
        return lastPosition;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper()
    {
        return mHelper == null ? new LinearLayoutHelper() : mHelper;
    }


    @Override
    public int getItemViewType(int position)
    {
        if (mList.isEmpty() && mEmptyView != null)
        {
            return EMPTY;
        }
        return super.getItemViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int position)
    {
        View layout = null;
        switch (getItemViewType(position))
        {
            case EMPTY:
                layout = mEmptyView;
                break;
            default:
                layout = LayoutInflater.from(mActivity.getApplicationContext()).inflate(mResourceLayout, parent, false);
                break;
        }

        return BaseViewHolder.getViewHolder(layout, mActivity);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position)
    {
        if (getItemViewType(position) == 0)
        {
            convert(holder, position, mList.get(position));
        }
    }

    @Override
    public int getItemCount()
    {
        return (mList.isEmpty() && mEmptyView != null) ? 1 : mList.size();
    }


    public abstract void convert(BaseViewHolder holder, int position, T t);
}
