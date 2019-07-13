package com.wei.cookbook.net;

import com.wei.cookbook.model.BaseBean;
import com.wei.cookbook.model.BaseResultBean;
import com.wei.cookbook.model.FoodBean;
import com.wei.cookbook.model.FoodTypeBean;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService
{
    //获取菜式列表
    @GET("cook/category")
    Flowable<BaseBean<List<FoodTypeBean>>> getFoodTypeData(@Query("key") String key);

    //获取菜品图文列表
    @GET("cook/index")
    Flowable<BaseResultBean<List<FoodBean>>> getFoodListData(@Query("key") String key, @Query("cid") int id);

    //关键字查询
    @GET("cook/query.php")
    Flowable<BaseResultBean<List<FoodBean>>> getFoodData(@Query("key") String key, @Query(("menu")) String menu);

}
