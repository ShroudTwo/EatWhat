package com.wei.cookbook.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils
{

    /*手机号码正则表达式验证*/
    public static boolean isMobile(String mobiles)
    {
        String telRegex = "^[1][3,4,5,7,8][0-9]{9}$";
        if (TextUtils.isEmpty(mobiles))
        {
            return false;
        } else
        {
            Pattern p = Pattern.compile(telRegex);
            Matcher m = p.matcher(mobiles);
            return m.matches();
        }
    }


    /*读取本地assets中Json文件读取本地assets中Json文件*/
    public static String getJson(Context context, String fileName)
    {
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getApplicationContext().getAssets();
        //使用IO流读取json文件内容
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /*判断字符串中是否包含该字符*/
    public static boolean isInclude(String descript, String include)
    {
        if (TextUtils.isEmpty(descript) || TextUtils.isEmpty(include)) return false;
        int index = descript.indexOf(include);
        return index != -1;
    }


    /*TextView字体分段的颜色和大小*/
    public static SpannableString getSpanUtils(Context context, String start, Object resource, int starColor, int endColor, int startSize, int endSize)
    {
        String end = String.valueOf(resource);
        SpannableString sp = new SpannableString(TextUtils.concat(start, end));
        sp.setSpan(new AbsoluteSizeSpan(startSize), 0, start.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(starColor), 0, start.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        sp.setSpan(new AbsoluteSizeSpan(endSize), start.length(), start.length() + end.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(endColor), start.length(), start.length() + end.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }


    /*Double保留两位小数位*/
    public static String getDouble2(double point)
    {
        DecimalFormat format = new DecimalFormat("#0.00");
        return format.format(point);
    }


}
