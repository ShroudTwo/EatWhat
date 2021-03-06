package com.wei.cookbook.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wei.cookbook.App;
import com.wei.cookbook.R;
import com.wei.cookbook.model.UserBean;
import com.wei.cookbook.net.BasePresenter;
import com.wei.cookbook.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 功能：登录页面
 */

public class LoginActivity extends BaseActivity implements TextWatcher
    {
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.et_passWord)
    EditText mEtPassWord;
    @Bind(R.id.tv_action)
    TextView mTvAction;

        //public static Tencent mTencent;

        private static final String mAppid = "1109537717";

        private static final String TAG = LoginActivity.class.getName();

        private String userId;
        private String userName = null;
        private String userIcon = null;
      //  private IUiListener listener;


    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_login;
    }

    @Override
    protected void setStatusBarColor()
    {

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
//        if (mTencent == null) {
//            mTencent = Tencent.createInstance(mAppid, this);
//        }

        mEtPhone.addTextChangedListener(this);
        mEtPassWord.addTextChangedListener(this);
        mTvAction.setClickable(false);

    }


    @OnClick({R.id.tv_action, R.id.tv_register, R.id.img_weixin, R.id.img_qq, R.id.img_weibo})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_action://登录
                /*判断当前状态是否符合登录状态*/
                String account = mEtPhone.getText().toString();
                String pass = mEtPassWord.getText().toString();
                String defaultIcon = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563188994572&di=2c0d2c191c17523243daa47a29f4fd60&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F9c63d61835dab92e4acac88c88eb6394b11da4471634-OtR7ja_fw658";
                UserBean user = new UserBean();
                user.setAccount(account);
                user.setPassWord(pass);
                user.setuNickName("未设置");
                user.setuIcon(defaultIcon);
                String admin = "123";
                if(account.equals(admin))
                {
                    alert(getString(R.string.alert_login_success));
                    App.setUser(user);
                    openActivity(MainActivity.class, null);
                    doFinish(200);
                }
                if (TextUtils.isEmpty(account) || (!StringUtils.isMobile(account)))
                {
                    alert(getString(R.string.alert_enter_phone));
                    return;
                }
                if (TextUtils.isEmpty(pass) || pass.length() < 6)
                {
                    alert(getString(R.string.alert_enter_passWord));
                    return;
                }
                if (user.verify())
                {
                    alert(getString(R.string.alert_login_success));
                    App.setUser(user);
                    openActivity(MainActivity.class, null);
                    doFinish(200);
                } else
                {
                    alert(getString(R.string.alert_login_failed));
                }
                break;
            case R.id.tv_register://注册
                openActivity(RegisterActivity.class, null);
                break;
            case R.id.img_weixin://微信
                //alert(getString(R.string.alert_have_no_function));
                weixinLogoin();
                break;
            case R.id.img_qq://QQ:
                //alert(getString(R.string.alert_have_no_function));
                qqLogin();
                break;
            case R.id.img_weibo://微博:
                //alert(getString(R.string.alert_have_no_function));
                sinaLogoin();
                break;
            default:
                break;
        }
    }

        //将drawable转化成字符串
        public synchronized static String drawableToString(Drawable drawable) {
            if (drawable != null) {
                Bitmap bitmap = Bitmap
                        .createBitmap(
                                drawable.getIntrinsicWidth(),
                                drawable.getIntrinsicHeight(),
                                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                        : Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight());
                drawable.draw(canvas);
                int size = bitmap.getWidth() * bitmap.getHeight() * 4;

                // 创建一个字节数组输出流,流的大小为size
                ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
                // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                // 将字节数组输出流转化为字节数组byte[]
                byte[] imagedata = baos.toByteArray();

                return Base64.encodeToString(imagedata, Base64.DEFAULT);
            }
            return " ";
        }


        private PlatformActionListener platformActionListener=new PlatformActionListener() {

            @Override

            public void onCancel(Platform arg0, int arg1) {
            }

            @Override

            public void onComplete(Platform arg0, int arg1, HashMap arg2) {

                PlatformDb platDB = arg0.getDb();//获取数平台数据DB
                //通过DB获取各种数据
                ///platDB.getToken();
                //platDB.getUserGender();
                userIcon = platDB.getUserIcon();
                userId = platDB.getUserId();
                userName = platDB.getUserName();
                UserBean user = new UserBean();
                user.setAccount(userId);
                user.setPassWord(null);
                user.setuIcon(userIcon);
                user.setuNickName(userName);
                App.setUser(user);
                openActivity(MainActivity.class, null);
            }

            @Override
            public void onError(Platform arg0, int arg1,Throwable arg2) {
            }

    };

        /**

         *新浪第三方登录

         */

        private void sinaLogoin() {
            Platform plat = ShareSDK.getPlatform(SinaWeibo.NAME);
            plat.removeAccount(true); //移除授权状态和本地缓存，下次授权会重新授权
            plat.SSOSetting(false); //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
            plat.setPlatformActionListener(platformActionListener);//授权回调监听，监听oncomplete，onerror，oncancel三种状态
            authorize(plat);

        }

        /**

         *微信第三方登录

         */

        private void weixinLogoin() {

            Platform plat = ShareSDK.getPlatform(Wechat.NAME);
            plat.removeAccount(true); //移除授权状态和本地缓存，下次授权会重新授权
            plat.SSOSetting(false); //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
            plat.setPlatformActionListener(platformActionListener);//授权回调监听，监听oncomplete，onerror，oncancel三种状态
            authorize(plat);
        }

        /**

         * QQ第三方登录

         */

        private void qqLogin() {

            Platform plat = ShareSDK.getPlatform(QQ.NAME);
            plat.removeAccount(true); //移除授权状态和本地缓存，下次授权会重新授权
            plat.SSOSetting(false); //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
            plat.setPlatformActionListener(platformActionListener);//授权回调监听，监听oncomplete，onerror，oncancel三种状态
            authorize(plat);
        }

        /**
         * 授权
         *
         * @param platform
         */
        private void authorize(Platform platform) {
            if (platform == null) {
                return;
            }
            if (platform.isAuthValid()) {  //如果授权就删除授权资料
                platform.removeAccount(true);
            }

            platform.showUser(null); //授权并获取用户信息
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
        String account = mEtPhone.getText().toString();
        String pass = mEtPassWord.getText().toString();
        //判断当前两个输入框是否均为空
        boolean isClick = ((!TextUtils.isEmpty(account)) && (!TextUtils.isEmpty(pass)));
        showActionBtn(isClick);
    }


    /*设置主按钮的功能和样式*/
    private void showActionBtn(boolean isClick)
    {
        if (mTvAction == null) return;
        mTvAction.setClickable(isClick);
        mTvAction.setBackgroundColor(getResources().getColor(isClick ? R.color.color : R.color.textBG));
    }


    //
//
//
//
//    @OnClick({R.id.tv_register, R.id.tv_btn, R.id.img_weixin, R.id.img_qq, R.id.img_weibo})
//    public void onViewClicked(View view)
//    {
//        switch (view.getId())
//        {
//            case R.id.tv_register:
//                startActivity(new Intent(this, RegisterActivity.class));
//                break;
//            case R.id.tv_btn:
//                //验证手机号码
//                String phone = mEdPhone.getText().toString();
//
//                if (phone.isEmpty() || phone.equals("")
//                        || phone.length() < 11
//                        || (!StringUtils.isMobile(phone)))
//                {
//                    ToastUtils.getUtils().showMsg(getString(R.string.toast_login_phone_no_length));
//                    return;
//                }
//                //验证密码格式
//                String pass = mEdPassWord.getText().toString();
//                if (pass.isEmpty() || pass.equals("")
//                        || pass.length() < 6)
//                {
//                    ToastUtils.getUtils().showMsg(getString(R.string.toast_login_pass_no_length));
//                    return;
//                }
////                验证账户
//                verifyUser(phone, pass);
//                break;
//            case R.id.img_weixin:
//                ToastUtils.getUtils().showMsg(getString(R.string.toast_login_thread));
//                break;
//            case R.id.img_qq:
//                ToastUtils.getUtils().showMsg(getString(R.string.toast_login_thread));
//                break;
//            case R.id.img_weibo:
//                ToastUtils.getUtils().showMsg(getString(R.string.toast_login_thread));
//                break;
//        }
//    }
//
//
//    //向服务器核实账户信息
//    private void verifyUser(String phone, final String pass)
//    {
//        UserBean mUser = UserBean.getCurrentUser();
//        if (mUser == null)
//        {
//            ToastUtils.getUtils().showMsg("本地暂无用户信息，请先注册...");
//        } else
//        {
//            if (mUser.getIphoneNumber().equals(phone) && mUser.getPassWord().equals(pass))
//            {
//                Intent it = new Intent(this, MainActivity.class);
//                startActivity(it);
//                finish();
//            } else
//            {
//                ToastUtils.getUtils().showMsg("用户名或密码错误，请检查后重试...");
//            }
//        }
//    }
//
//
}
