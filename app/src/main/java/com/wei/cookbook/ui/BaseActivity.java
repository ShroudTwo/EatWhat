package com.wei.cookbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wei.cookbook.R;
import com.wei.cookbook.net.BasePresenter;
import com.wei.cookbook.net.BaseView;
import com.wei.cookbook.utils.ActivityManager;
import com.wei.cookbook.utils.MsgDialog;
import com.wei.cookbook.utils.ProgressDialog;
import com.wei.cookbook.utils.StatusBarUtils;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseActivity<T extends BasePresenter> extends RxAppCompatActivity implements BaseView
{
    /*获取布局资源*/
    protected abstract int getLayoutResource();

    /*设置状态栏颜色*/
    protected abstract void setStatusBarColor();

    /*获取Presenter层对象*/
    protected abstract T createPresenter();

    /*消息弹窗*/
    private MsgDialog mMsgDialog;
    private ProgressDialog mProgressDialog;


    protected T mPresenter;
    protected BaseActivity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        ActivityManager.getManager().addActivity(this);
        setContentView(getLayoutResource());
        mActivity = this;
        ButterKnife.bind(this);
        initView(savedInstanceState);
        initData();
    }

    @Override
    public void setContentView(int layoutResID)
    {
        setStatusBarColor();
        StatusBarUtils.setLightMode(this);
        super.setContentView(layoutResID);
    }

    /*初始化View*/
    protected void initView(Bundle savedInstanceState)
    {

    }

    /*网络请求以及初始化数据*/
    protected void initData()
    {
    }

    private static long lastClickTime;//按钮点击记录时间
    private static final long TIME_CLICK = 800L;

    /*避免重复点击*/
    public boolean isFastDoubleClick()
    {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < TIME_CLICK)
        {
            return true;
        } else
        {
            lastClickTime = time;
            return false;
        }
    }

    /*初始化Dialog弹窗*/
    public void showMsgDialog(String msg, View.OnClickListener listener)
    {
        if (mMsgDialog == null)
        {
            mMsgDialog = new MsgDialog(this);
        }
        mMsgDialog.setTitle(msg);
        mMsgDialog.showDialog(listener);
    }

    /*关闭Dialog弹窗*/
    public void disMsgDialog()
    {
        if (mMsgDialog != null && mMsgDialog.isShowing())
        {
            mMsgDialog.dismiss();
        }
    }


    /*点击关闭Activity*/
    protected void setLeftBack()
    {
        View finish = findViewById(R.id.layout_finish);
        if (finish != null)
        {
            finish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doFinish();
            }
        });
        }
    }

    /*设置顶部标题*/
    protected void setTitle(String text)
    {
        if (TextUtils.isEmpty(text)) return;
        TextView title = findViewById(R.id.layout_title);
        if (title != null)
        {
            title.setText(text);
        }
    }


    /*Toast弹窗*/
    protected void alert(String msg)
    {
        if (TextUtils.isEmpty(msg)) return;
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    /*关闭*/
    protected void doFinish()
    {
        this.finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /*延迟关闭*/
    protected void doFinish(long delay)
    {
        Observable.interval(delay, TimeUnit.MILLISECONDS).take(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Long>()
                {
                    @Override
                    public void accept(Long aLong) throws Exception
                    {
                        doFinish();
                    }
                });
    }

    /*启动Activity*/
    protected void openActivity(Class<? extends BaseActivity> cls, Parcelable parcelable)
    {
        Intent it = new Intent(this, cls);
        if (parcelable != null)
        {
            it.putExtra(cls.getSimpleName(), parcelable);
        }
        startActivity(it);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    /*启动Activity*/
    protected void openActivityForResult(Class<? extends BaseActivity> cls, Parcelable parcelable, int requestCode)
    {
        Intent it = new Intent(this, cls);
        if (parcelable != null)
        {
            it.putExtra(cls.getSimpleName(), parcelable);
        }
        startActivityForResult(it, requestCode);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    private static long exitTime = 0;//点击后退记录时间

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (this instanceof MainActivity && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if ((System.currentTimeMillis() - exitTime) > 2000)
            {
                alert("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else
            {
                ActivityManager.getManager().finishAllActivity();
            }
            return true;
        }
        /*监听返回按键，实现跳转动画*/
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            doFinish(200);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void showErrorMsg(String msg)
    {
        alert(msg);
    }


    @Override
    public void showError()
    {

    }

    @Override
    public void showLoading()
    {
        if (mProgressDialog == null)
        {
            mProgressDialog = new ProgressDialog(mActivity);
        }
        if (mProgressDialog.isShowing())
        {
            return;
        }
        mProgressDialog.show();
    }

    @Override
    public void dissLoading()
    {
        if (mProgressDialog != null && mProgressDialog.isShowing())
        {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showContent()
    {

    }

    @Override
    public void showData(Object o)
    {
    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
        disMsgDialog();
        if (mPresenter != null)
        {
            mPresenter.detach();
        }
        ActivityManager.getManager().finishActivity(this);
    }
}
