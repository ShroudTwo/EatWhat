package com.wei.cookbook.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.wei.cookbook.App;
import com.wei.cookbook.R;
import com.wei.cookbook.config.Config;
import com.wei.cookbook.model.BaseBean;
import com.wei.cookbook.model.CommentBean;
import com.wei.cookbook.model.FoodBean;
import com.wei.cookbook.net.FoodPresenter;
import com.wei.cookbook.ui.adapter.BaseDelegateAdapter;
import com.wei.cookbook.ui.adapter.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.wei.cookbook.R;
import com.wei.cookbook.net.BasePresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * 功能：菜谱详情
 */

public class FoodDetailActivity extends BaseActivity<FoodPresenter>
{
    @Bind(R.id.img_collect)
    ImageView mImgCollect;
    @Bind(R.id.ed_comment)
    EditText mEdComment;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private static final String TAG = FoodDetailActivity.class .getSimpleName();
    private FoodBean mData = null;
    private String steps = "";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    int now = 0;
    int i=0;
    String [] sstep = new String [20];
    private String smallsteps = "开始语音播报。";
    private static String dishName;
    Bitmap mBitmap = null;
    String mDirFileName = null;
    private DelegateAdapter mAdapter;
    private BaseDelegateAdapter mDetailAdapter, mStepAdapter, mCommentsTitleAdapter, mCommentsAdapter;


    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String , String>();
    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_food_detail;
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
        initSpeech() ;
//        verifyAudioPermissions(this);
        super.initView(savedInstanceState);
        mData = getIntent().getParcelableExtra(FoodDetailActivity.class.getSimpleName());
        setLeftBack();
        mImgCollect.setImageResource((mData != null && mData.isCollected()) ? R.drawable.tab_faved : R.drawable.tab_fav);
        setTitle(mData != null ? mData.getTitle() : "");
        VirtualLayoutManager manager = new VirtualLayoutManager(mActivity);
        manager.setInitialPrefetchItemCount(5);
        mRecyclerView.setLayoutManager(manager);
        //关闭动效提升效率
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        //Item高度固定，避免浪费资源
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(5);

        mAdapter = new DelegateAdapter(manager);
        mRecyclerView.setAdapter(mAdapter);

        initAdapter();
    }




    private void initAdapter()
    {
        /*菜品介绍*/
        mDetailAdapter = new BaseDelegateAdapter<FoodBean>(this, R.layout.layout_food_detail_introduce)
        {
            @Override
            public void convert(BaseViewHolder holder, int position, FoodBean data)
            {
                List<String> list = data.getAlbums();
                dishName = data.getTitle();
                holder.setCircleImageResource((list != null && list.size() != 0) ? list.get(0) : "",
                        R.dimen.dp_62, R.id.item_image)
                        .setText(data.getTitle(), R.id.item_title)
                        .setText(data.getTags(), R.id.item_descript)
                        .setText(data.getImtro(), R.id.item_text);
                TextView introduce = holder.getView(R.id.layout).findViewById(R.id.item_introduce);
                introduce.setText(getString(R.string.tv_introduce));
            }
        };
        mAdapter.addAdapter(mDetailAdapter);
        mDetailAdapter.setData(mData);

        /*菜品做法步骤*/
        mStepAdapter = new BaseDelegateAdapter<FoodBean>(this, R.layout.layout_food_detail_steps)
        {
            @Override
            public void convert(BaseViewHolder holder, int position, FoodBean data)
            {
                //TODO 照理说这块应该放在子线程去生成做法字符串
                //TODO 但是我不想改了
                StringBuilder builder = new StringBuilder();
                if (data.getSteps() != null && (!data.getSteps().isEmpty()))
                {
                    i =0;
                    for (FoodBean.StepsBean step : data.getSteps())
                    {
                        builder.append(step.getStep()).append("\n");
                        sstep[i++] = step.getStep();
                    }

                }
                holder.setText(builder, R.id.item_text);
                TextView introduce = holder.getView(R.id.layout).findViewById(R.id.item_introduce);
                introduce.setText(getString(R.string.tv_steps));

            }
        };
        mAdapter.addAdapter(mStepAdapter);
        mStepAdapter.setData(mData);

        /*评论标题*/
        mCommentsTitleAdapter = new BaseDelegateAdapter<String>(this, R.layout.layout_detail_introduce)
        {
            @Override
            public void convert(BaseViewHolder holder, int position, String s)
            {
                holder.setText(s, R.id.item_introduce);
            }
        };
        mAdapter.addAdapter(mCommentsTitleAdapter);
        mCommentsTitleAdapter.setData(getString(R.string.tv_comments));

        /*评论*/
        mCommentsAdapter = new BaseDelegateAdapter<CommentBean>(this, R.layout.item_comment)
        {
            @Override
            public void convert(BaseViewHolder holder, int position, CommentBean data)
            {
                holder.setCircleImageResource(data.getAvartar(), R.dimen.dp_30, R.id.item_image)
                        .setText(data.getNickName(), R.id.item_name)
                        .setText(data.getContent(), R.id.item_descript);
            }
        };
        mAdapter.addAdapter(mCommentsAdapter);
    }
    @Override
    protected void initData()
    {
        super.initData();
        mPresenter.getCommentsData();
    }
    @Override
    public void showData(Object o)
    {
        super.showData(o);
        if (o instanceof List)
        {
            List<CommentBean> list = (List<CommentBean>) o;
            mCommentsAdapter.setData(list);
        }
    }
    @OnClick({R.id.img_collect, R.id.tv_commit, R.id.img_voice, R.id.img_share})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.img_collect:
                final boolean collect = (mData != null && mData.isCollected());
                showMsgDialog(getString(collect ? R.string.dialog_cancel_collect : R.string.dialog_collect), new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (collect)
                        {
                            alert(getString(R.string.alert_cancel_success));
                            mData.cancel();
                        } else
                        {
                            alert(getString(R.string.alert_collect_success));
                            mData.collect();
                        }
                        mImgCollect.setImageResource((mData != null && mData.isCollected()) ? R.drawable.tab_faved : R.drawable.tab_fav);
                        BaseBean data = new BaseBean();
                        data.setMsg(CollectActivity.class.getSimpleName());
                        EventBus.getDefault().post(data);
                        disMsgDialog();

                    }
                });
                break;
            case R.id.tv_commit:
                String descript = mEdComment.getText().toString();
                if (TextUtils.isEmpty(descript))
                {
                    alert(getString(R.string.alert_enter_comments));
                    return;
                }
                CommentBean data = new CommentBean();
                data.setAvartar(Config.BASE_HEADE);
                data.setNickName(App.getCacheUser() != null ? App.getCacheUser().getAccount() : "");
                data.setContent(descript);
                mCommentsAdapter.setData(mCommentsAdapter.getData().size(), data);
                mEdComment.setText("");
                mRecyclerView.scrollToPosition(mCommentsAdapter.getData().size());
                break;
            case R.id.img_share:
                mBitmap = shotRecyclerView(mRecyclerView);
                mDirFileName = saveBitmap(mBitmap);
                showShare(null, mDirFileName);
                break;
            case R.id.img_voice:
                //openActivity(VoiceActivity.class,null);
                speekcaipu();
                break;
        }
    }
    private void speekcaipu() {
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer( this, null);
        mTts.setParameter(SpeechConstant. VOICE_NAME, "xiaoyan" ); // 设置发音人
        mTts.setParameter(SpeechConstant. SPEED, "38" );// 设置语速
        mTts.setParameter(SpeechConstant. VOLUME, "80" );// 设置音量，范围 0~100
        mTts.setParameter(SpeechConstant. ENGINE_TYPE, SpeechConstant. TYPE_CLOUD); //设置云端
        mTts.startSpeaking( smallsteps, new FoodDetailActivity.MySynthesizerListener()) ;
        nextsteps();
    }

    class MySynthesizerListener implements SynthesizerListener {

        @Override
        public void onSpeakBegin() {
            showTip(" 开始播放 ");
        }

        @Override
        public void onSpeakPaused() {
            showTip(" 暂停播放 ");
        }

        @Override
        public void onSpeakResumed() {
            showTip(" 继续播放 ");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成 ");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    }
        private void startSpeechDialog() {
            //1. 创建RecognizerDialog对象
            RecognizerDialog mDialog = new RecognizerDialog(this, new FoodDetailActivity.MyInitListener()) ;
            //2. 设置accent、 language等参数
            mDialog.setParameter(SpeechConstant. LANGUAGE, "zh_cn" );// 设置中文
            mDialog.setParameter(SpeechConstant. ACCENT, "mandarin" );
            // 若要将UI控件用于语义理解，必须添加以下参数设置，设置之后 onResult回调返回将是语义理解结果
            // mDialog.setParameter("asr_sch", "1");
            // mDialog.setParameter("nlp_version", "2.0");
            //3.设置回调接口
            mDialog.setListener( new FoodDetailActivity.MyRecognizerDialogListener()) ;
            //4. 显示dialog，接收语音输入
            mDialog.show() ;
        }
        class MyRecognizerDialogListener implements RecognizerDialogListener {
            /**
             * @param results
             * @param isLast  是否说完了
             */
            @Override
            public void onResult(RecognizerResult results, boolean isLast) {
                String result = results.getResultString(); //为解析的
                //showTip(result) ;
                System. out.println(" 没有解析的 :" + result);

                String text = JsonParser.parseIatResult(result) ;//解析过后的
                System. out.println(" 解析后的 :" + text);

                String sn = null;
                //读取json结果中的 sn字段
                try {
                    JSONObject resultJson = new JSONObject(results.getResultString()) ;
                    sn = resultJson.optString("sn" );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mIatResults .put(sn, text) ;//没有得到一句，添加到
                StringBuffer resultBuffer = new StringBuffer();
                for (String key : mIatResults.keySet()) {
                    resultBuffer.append(mIatResults .get(key));
                }
                String ss = resultBuffer.toString();
                if(ss.equals("下一步。")||ss.equals("下一步")||ss.equals("1。"))
                {
                    nextsteps();
                    ss = "0";
                }
            }
            @Override
            public void onError(SpeechError speechError) {
            }
        }
        class MyInitListener implements InitListener {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    showTip("初始化失败 ");
                }
            }
        }
        /**
         * 语音识别
         */
        private void nextsteps() {
            i++;
            if (sstep[i] != null)
            {
                smallsteps = sstep[i];
              }
            else
            {
                i = 0;
                smallsteps = sstep[i];
            }
        }
        private void startSpeech() {
            //1. 创建SpeechRecognizer对象，第二个参数： 本地识别时传 InitListener
            SpeechRecognizer mIat = SpeechRecognizer.createRecognizer( this, null); //语音识别器
            //2. 设置听写参数，详见《 MSC Reference Manual》 SpeechConstant类
            mIat.setParameter(SpeechConstant. DOMAIN, "iat" );// 短信和日常用语： iat (默认)
            mIat.setParameter(SpeechConstant. LANGUAGE, "zh_cn" );// 设置中文
            mIat.setParameter(SpeechConstant. ACCENT, "mandarin" );// 设置普通话
            //3. 开始听写
            mIat.startListening( mRecoListener);
        }
        // 听写监听器
        private RecognizerListener mRecoListener = new RecognizerListener() {
            // 听写结果回调接口 (返回Json 格式结果，用户可参见附录 13.1)；
            //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
            //关于解析Json的代码可参见 Demo中JsonParser 类；
            //isLast等于true 时会话结束。
            public void onResult(RecognizerResult results, boolean isLast) {
                Log.e (TAG, results.getResultString());
                System.out.println(results.getResultString()) ;
                showTip(results.getResultString()) ;
            }

            // 会话发生错误回调接口
            public void onError(SpeechError error) {
                showTip(error.getPlainDescription(true)) ;
                // 获取错误码描述
                Log. e(TAG, "error.getPlainDescription(true)==" + error.getPlainDescription(true ));
            }

            // 开始录音
            public void onBeginOfSpeech() {
                showTip(" 开始录音 ");
            }

            //volume 音量值0~30， data音频数据
            public void onVolumeChanged(int volume, byte[] data) {
                showTip(" 声音改变了 ");
            }

            // 结束录音
            public void onEndOfSpeech() {
                showTip(" 结束录音 ");
            }

            // 扩展用接口
            public void onEvent(int eventType, int arg1 , int arg2, Bundle obj) {
            }
        };
        private void showTip (String data) {
            Toast.makeText( this, data, Toast.LENGTH_SHORT).show() ;
        }
//        private void nextsteps() {
//            if (now == 1) {
//                res = a;
//                now = 2;
//            }
//            else if(now == 2) {
//                res = b;
//                now = 3;
//            }
//            else if(now == 3) {
//                res = c;
//                now = 4;
//            }
//            else if(now==4) {
//                res = d;
//                now = 1;
//            }
//        }
//    //申请录音权限
//    private static final int GET_RECODE_AUDIO = 1;
//    private static String[] PERMISSION_AUDIO = {
//            Manifest.permission.RECORD_AUDIO
//    };
//
//    /* 申请录音，存储写权限*/
//    public static void verifyAudioPermissions(Activity activity) {
//        int permission = ActivityCompat.checkSelfPermission(activity,
//                Manifest.permission.RECORD_AUDIO);
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(activity, PERMISSION_AUDIO,
//                    GET_RECODE_AUDIO);
//        }
//
//    }


    private void initSpeech() {
        // 将“12345678”替换成您申请的 APPID，申请地址： http://www.xfyun.cn
        // 请勿在 “ =”与 appid 之间添加任务空字符或者转义符
        SpeechUtility. createUtility( this, SpeechConstant. APPID + "=5d0330df" );
    }

    /**
     * RecyclerView截长屏
     * */
    public static Bitmap shotRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                        holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            Drawable lBackground = view.getBackground();
            if (lBackground instanceof ColorDrawable) {
                ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                int lColor = lColorDrawable.getColor();
                bigCanvas.drawColor(lColor);
            }

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }
        }
        return bigBitmap;
    }

    /**
     * 保存RecyclerView截长屏图片
     * */
    public String saveBitmap(Bitmap bitmap) {
        // 首先保存图片
        String sdcardPath = System.getenv("EXTERNAL_STORAGE");
        String dir = sdcardPath + "/吃点啥/";
        File appDir = new File(dir);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String fileName = simpleDateFormat.format(date) + "_" + dishName + ".png";
        String dirFileName = dir + fileName;
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "/sdcard/namecard/")));
        return dirFileName;
    }
    /**
     *一键分享
     **/
    private void showShare(String platform, String dirFileName) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        //oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(dishName);
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        //oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("~~入手" + dishName + "的正确姿势~~");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(dirFileName);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        //oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        //oks.setSiteUrl("http://sharesdk.cn");

        //启动分享
        oks.show(this);
    }


}