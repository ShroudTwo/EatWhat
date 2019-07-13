package com.wei.cookbook.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wei.cookbook.R;


public class MsgDialog extends Dialog
{
    private TextView mTitle, mCancel, mAction;

    public MsgDialog(Context context)
    {
        super(context, R.style.DialogStyle);
        initView(context);
    }


    /*初始化Dialog*/
    private void initView(Context context)
    {
        View layout = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.dialog_msg, null);
        setContentView(layout);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        mTitle = layout.findViewById(R.id.dialog_title);
        mCancel = layout.findViewById(R.id.dialog_cancel);
        mAction = layout.findViewById(R.id.dialog_action);
        //设置点击监听
        mCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
    }

    /*设置标题*/
    public MsgDialog setTitle(String msg)
    {
        if (TextUtils.isEmpty(msg) || mTitle == null) return this;
        mTitle.setText(msg);
        return this;
    }

    /*设置确定按钮文字*/
    public MsgDialog setAction(String msg)
    {
        if (TextUtils.isEmpty(msg) || mAction == null) return this;
        mAction.setText(msg);
        return this;
    }


    /*显示Dialog*/
    public void showDialog(View.OnClickListener listener)
    {
        if (isShowing()) return;
        if (mAction!=null&&listener!=null)
        {
            mAction.setOnClickListener(listener);
        }
        show();
    }
}
