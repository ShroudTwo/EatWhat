package com.wei.cookbook.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.wei.cookbook.R;



public class ProgressDialog extends Dialog
{
    private AnimationDrawable mLoading;

    public ProgressDialog(Context context)
    {
        super(context, R.style.DialogStyle);
        initView(context);
    }


    private void initView(Context context)
    {
        View layout = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.dialog_loading, null);
        setContentView(layout);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        ImageView loading = layout.findViewById(R.id.loading);
        mLoading = (AnimationDrawable) loading.getDrawable();
        setOnDismissListener(new OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                if (mLoading != null && mLoading.isRunning())
                {
                    mLoading.stop();
                }
            }
        });
    }

    @Override
    public void show()
    {
        if (isShowing())
        {
            return;
        }
        if (mLoading != null && !mLoading.isRunning())
        {
            mLoading.start();
        }
        super.show();
    }

    @Override
    public void dismiss()
    {
        super.dismiss();
        if (mLoading != null && mLoading.isRunning())
        {
            mLoading.stop();
        }
    }

}
