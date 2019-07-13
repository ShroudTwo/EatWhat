package com.wei.cookbook.net.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;



public class CodeInterceptor implements Interceptor
{
    public CodeInterceptor()
    {

    }

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        Response response = chain.proceed(request);
        int code = response.code();
        if (code != 200 && code != 201)
        {
            return response.newBuilder()
                    .code(200)
                    .request(request)
                    .body(ResponseBody.create(response.body().contentType(), response.body().string()))
                    .build();
        }
        return response;
    }
}
