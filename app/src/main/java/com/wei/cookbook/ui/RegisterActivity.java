package com.wei.cookbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wei.cookbook.R;
import com.wei.cookbook.model.UserBean;
import com.wei.cookbook.net.BasePresenter;
import com.wei.cookbook.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class RegisterActivity extends BaseActivity implements TextWatcher
{
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.tv_code)
    TextView mTvCode;
    @Bind(R.id.et_code)
    EditText mEtCode;
    @Bind(R.id.et_passWord)
    EditText mEtPassWord;
    @Bind(R.id.tv_action)
    TextView mTvAction;
    @Bind(R.id.tv_question)
    TextView mTvQuestion;

    /*当前是否正在进行验证码倒计时*/
    private boolean isCoding = false;

    private boolean isCheck = false;
    private boolean isGet = false;
    /*短信倒计时器*/
    private Timer mTimer = new Timer();

    //public EventHandler eh; //事件接收器

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_register;
    }

    @Override
    protected void setStatusBarColor()
    {

    }

    @Override
    protected void initData()
    {

    }

    private void initHandler()
    {
        EventHandler eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                new Handler(Looper.getMainLooper(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        int event = msg.arg1;
                        int result = msg.arg2;
                        Object data = msg.obj;
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理成功得到验证码的结果
                                // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                                isGet = true;
                            } else {
                                // TODO 处理错误的结果
                                ((Throwable) data).printStackTrace();
                            }
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理验证码验证通过的结果
                                isCheck = true;
                            } else {
                                // TODO 处理错误的结果
                                ((Throwable) data).printStackTrace();
                            }
                        }
                        // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                        return false;
                    }
                }).sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler); //注册短信回调
    }

    @Override
    protected BasePresenter createPresenter()
    {
        return null;
    }


    @Override
    protected void initView(Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        initHandler();
        setLeftBack();
        mEtPhone.addTextChangedListener(this);
        mEtCode.addTextChangedListener(this);
        mEtPassWord.addTextChangedListener(this);
        mTvCode.setClickable(false);
        mTvAction.setClickable(false);
    }

    @OnClick({R.id.tv_code, R.id.tv_action, R.id.tv_toast})
    public void onViewClicked(View view)
    {
        if (isFastDoubleClick()) return;
        String account = mEtPhone.getText().toString();
        if (TextUtils.isEmpty(account) || (!StringUtils.isMobile(account)))
        {
            alert(getString(R.string.alert_enter_phone));
            return;
        }
        switch (view.getId())
        {
            case R.id.tv_code://获取手机验证码
                    if (StringUtils.isMobile(account)) {
                        SMSSDK.getVerificationCode("+86",mEtPhone.getText().toString());//获取验证码
                        mTimer.start();
                    }else{
                        alert(getString(R.string.alert_enter_phone));
                    }
                break;
            case R.id.tv_action://确认注册账号
                    SMSSDK.submitVerificationCode("+86",mEtPhone.getText().toString().trim(),mEtCode.getText().toString().trim());//提交验证

                String pass = mEtPassWord.getText().toString();
                if (TextUtils.isEmpty(pass) || pass.length() < 6)
                {
                    alert(getString(R.string.alert_enter_passWord));
                    return;
                }
                UserBean user = new UserBean();
                user.setAccount(account);
                user.setPassWord(pass);
                if (user.register() && isCheck)
                {
                    alert(getString(R.string.alert_register_success));
                    doFinish(200);
                }else
                {
                    if(!isGet)
                    {
                        alert("未获取验证码");
                    }
                    else if(!isCheck) {
                        alert("验证码错误");
                    }
                    else
                    {
                        alert(getString(R.string.alert_register_failed));
                    }

                }
                break;
            case R.id.tv_toast://用户协议
                alert(getString(R.string.alert_have_no_function));
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        setCodeBtn();
        setActionBtn();
    }

    /*设置获取验证码按钮UI样式*/
    private void setCodeBtn()
    {
        String account = mEtPhone.getText().toString();
        boolean click = (!TextUtils.isEmpty(account)) && account.length() == 11 && (!isCoding);
        mTvCode.setClickable(click);
        mTvCode.setBackgroundColor(getResources().getColor(click ? R.color.color : R.color.textBG));
        mTvCode.setTextColor(getResources().getColor(click ? R.color.black : R.color.textUnSelected));
    }

    /*设置充值密码按钮UI演示*/
    private void setActionBtn()
    {
        String account = mEtPhone.getText().toString();
        String code = mEtCode.getText().toString();
        String pass = mEtPassWord.getText().toString();
        boolean isClick = (!TextUtils.isEmpty(account)) && (!TextUtils.isEmpty(code)) && (!TextUtils.isEmpty(pass));
        mTvAction.setClickable(isClick);
        mTvAction.setBackgroundColor(getResources().getColor(isClick ? R.color.color : R.color.textBG));
        mTvAction.setTextColor(getResources().getColor(isClick ? R.color.black : R.color.textUnSelected));
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mTimer != null)
        {
            mTimer.cancel();
            isCoding = false;
        }
       // SMSSDK.unregisterEventHandler();

    }

    /*短信验证码倒计时器*/
    public class Timer extends CountDownTimer
    {
        public Timer()
        {
            super(60 * 1000, 1000);
        }

        @Override
        public void onTick(long times)
        {
            StringBuilder builder = new StringBuilder();
            builder.append(times / 1000).append("s");
            mTvCode.setText(builder.toString());
            isCoding = true;
            setCodeBtn();
        }

        @Override
        public void onFinish()
        {
            isCoding = false;
            setCodeBtn();
            mTvCode.setText(getString(R.string.tv_getCode));
        }
    }
}

