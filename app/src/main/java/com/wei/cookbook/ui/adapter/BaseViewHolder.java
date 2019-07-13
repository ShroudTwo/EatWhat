package com.wei.cookbook.ui.adapter;

import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.wei.cookbook.ui.BaseActivity;
import com.wei.cookbook.utils.ImageUtils;

import java.io.File;

import butterknife.ButterKnife;


public class BaseViewHolder extends RecyclerView.ViewHolder
{
    private final SparseArray<View> views;
    private ImageUtils mUtils;


    private BaseViewHolder(BaseActivity activity, View iteview)
    {
        super(iteview);
        ButterKnife.bind(this, iteview);
        views = new SparseArray<>();
        mUtils = new ImageUtils(activity);
    }

    public static BaseViewHolder getViewHolder(View iteview, BaseActivity activity)
    {
        return new BaseViewHolder(activity, iteview);
    }


    public <T extends View> T getView(int viewId)
    {
        View view = views.get(viewId);
        if (view == null)
        {
            view = itemView.findViewById(viewId);
            if (view != null)
            {
                views.put(viewId, view);
            }
        }
        return (T) view;
    }

    /**
     * Will set an picture of the ImageView for the picture link
     *
     * @param imageUrls The link of the Picture
     * @param viewId    The ImageView id
     * @return The BaseViewHolder for chaining
     */
    public BaseViewHolder setImageResource(String imageUrls, @IdRes int viewId)
    {
        if (TextUtils.isEmpty(imageUrls)) return this;
        ImageView view = getView(viewId);
        if (view != null)
        {
            mUtils.setImageResource(imageUrls, view);
        }
        return this;
    }


    public BaseViewHolder setImageFileResource(File file, @IdRes int viewId)
    {
        if (file == null || !file.exists()) return this;
        ImageView view = getView(viewId);
        if (view != null)
        {
            mUtils.setImageFileResource(file, view);
        }
        return this;
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param resourceId The image resource id.
     * @param viewId     The view id.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setImageResource(int resourceId, @IdRes int viewId)
    {
        ImageView view = getView(viewId);
        if (view != null)
        {
            view.setImageResource(resourceId);
        }
        return this;
    }

    /**
     * Will set Circle Picture of the ImageView
     *
     * @param imageUrls The link of the picture
     * @param viewId    The ImageView id
     * @return The BaseViewHolder for chaining
     */
    public BaseViewHolder setCircleImageResource(String imageUrls, @DimenRes int resize, @IdRes int viewId)
    {
        if (TextUtils.isEmpty(imageUrls)) return this;
        ImageView view = getView(viewId);
        if (view != null)
        {
            mUtils.setCircleImageResource(imageUrls, resize, view);
        }
        return this;
    }


    /**
     * 加载圆角图片
     *
     * @param imageUrls
     * @param viewId
     * @param round
     * @return
     */
    public BaseViewHolder setRoundImageResource(String imageUrls, int round, @IdRes int viewId)
    {
        if (TextUtils.isEmpty(imageUrls)) return this;
        ImageView view = getView(viewId);
        if (view != null)
        {
            mUtils.setRoundImageResource(imageUrls, round, view);
        }
        return this;
    }

    /**
     * Will set the Text of the TextVeiw
     *
     * @param text   The text to put in the TextView
     * @param viewId The View id
     * @return The BaseViewHolder for chaining
     */
    public BaseViewHolder setText(CharSequence text, @IdRes int viewId)
    {
        if (TextUtils.isEmpty(text)) return this;
        TextView view = getView(viewId);
        if (view != null)
        {
            view.setText(text);
        }
        return this;
    }

    /**
     * Will set the Text of the TextVeiw
     *
     * @param text   The text to put in the TextView
     * @param viewId The View id
     * @return The BaseViewHolder for chaining
     */
    public BaseViewHolder setText(SpannableString text, @IdRes int viewId)
    {
        if (text == null) return this;
        TextView view = getView(viewId);
        if (view != null)
        {
            view.setText(text);
        }
        return this;
    }

    /**
     * Will set background color of a view.
     *
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setBackgroundColor(@ColorInt int color, @IdRes int viewId)
    {
        View view = getView(viewId);
        if (view != null)
        {
            view.setBackgroundColor(color);
        }
        return this;
    }

    /**
     * Will set background drawable of a view.
     *
     * @param viewId The view id.
     * @param resId  A drawable, not a color id.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setBackgroundResource(int resId, @IdRes int viewId)
    {
        View view = getView(viewId);
        if (view != null)
        {
            view.setBackgroundResource(resId);
        }
        return this;
    }


    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setTextColor(@ColorInt int textColor, @IdRes int viewId)
    {
        TextView view = getView(viewId);
        if (view != null)
        {
            view.setTextColor(textColor);
        }
        return this;
    }


    /**
     * Set a view visibility to VISIBLE (true) or INVISIBLE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for INVISIBLE.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setVisible(@IdRes int viewId, boolean visible)
    {
        View view = getView(viewId);
        if (view != null)
        {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
        return this;
    }


    /**
     * Set a view visibility to VISIBLE (true) or INVISIBLE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for INVISIBLE.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setVisible(@IdRes int viewId, int visible)
    {
        View view = getView(viewId);
        if (view != null)
        {
            view.setVisibility(visible);
        }
        return this;
    }

    /**
     * Sets the on click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on click listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnClickListener(@IdRes int viewId, View.OnClickListener listener)
    {
        View view = getView(viewId);
        if (view != null && listener != null)
        {
            view.setOnClickListener(listener);
        }
        return this;
    }

}
