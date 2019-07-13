package com.wei.cookbook.net.interceptor;

import android.content.Context;


import com.wei.cookbook.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class CacheInterceptor implements Interceptor
{

    private Context mContext;


    public CacheInterceptor(Context context)
    {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        boolean available = NetworkUtils.isAvailable(mContext);
        if (!available)
        {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (available)
        {
            int maxAge = 0;// 有网络时 设置缓存超时时间0个小时
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Pragma")// 清除头信息
                    .build();
        } else
        {
            int maxStale = 60 * 60 * 24 * 7;        // 无网络时，设置超时为7天
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
        }
        return response;
    }
}
