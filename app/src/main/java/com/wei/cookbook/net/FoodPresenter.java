package com.wei.cookbook.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.wei.cookbook.App;
import com.wei.cookbook.config.Config;
import com.wei.cookbook.model.BaseBean;
import com.wei.cookbook.model.BaseResultBean;
import com.wei.cookbook.model.CommentBean;
import com.wei.cookbook.model.FoodBean;
import com.wei.cookbook.model.FoodTypeBean;
import com.wei.cookbook.sql.CommentBeanDao;
import com.wei.cookbook.sql.FoodBeanDao;
import com.wei.cookbook.utils.RxUtils;
import com.wei.cookbook.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;


public class FoodPresenter extends BasePresenter<BaseView>
{
    public FoodPresenter(BaseView view, LifecycleProvider<ActivityEvent> provider)
    {
        super(view, provider);
    }


    /*获取菜谱的分类*/
    public void getFoodTypeData()
    {
        add(
                RetrofitManager.getManager().getService().getFoodTypeData(Config.HTTP_NET_KEY)
                        .compose(this.<BaseBean<List<FoodTypeBean>>>loadTransformer())
                        .compose(RxUtils.<List<FoodTypeBean>>handleBaseData())
                        .subscribeWith(new BaseObserver<List<FoodTypeBean>>(mView,true)
                        {
                            @Override
                            public void onNext(List<FoodTypeBean> list)
                            {
                                if (mView != null)
                                {
                                    mView.showData(list);
                                }
                            }
                        })
        );
    }

    /*获取菜品图文列表*/
    public void getFoodListData(int id)
    {
        add(
                RetrofitManager.getManager().getService().getFoodListData(Config.HTTP_NET_KEY, id)
                        .compose(this.<BaseResultBean<List<FoodBean>>>loadTransformer())
                        .compose(RxUtils.<List<FoodBean>>handleBaseResult())
                        .subscribeWith(new BaseObserver<List<FoodBean>>(mView)
                        {
                            @Override
                            public void onNext(List<FoodBean> list)
                            {
                                if (mView != null)
                                {
                                    mView.showData(list);
                                }
                            }
                        })
        );
    }

    /*获取搜索后的菜品图文列表*/
    public void getSearchFoodData(String title)
    {
        add(
                RetrofitManager.getManager().getService().getFoodData(Config.HTTP_NET_KEY,title)
                        .compose(this.<BaseResultBean<List<FoodBean>>>loadTransformer())
                        .compose(RxUtils.<List<FoodBean>>handleBaseResult())
                        .subscribeWith(new BaseObserver<List<FoodBean>>(mView)
                        {
                            @Override
                            public void onNext(List<FoodBean> list)
                            {
                                if (mView != null)
                                {
                                    mView.showData(list);
                                }
                            }
                        })
        );
    }

    /*获取评论信息*/
    public void getCommentsData()
    {
        add(
                Flowable.create(new FlowableOnSubscribe<List<CommentBean>>()
                {
                    @Override
                    public void subscribe(FlowableEmitter<List<CommentBean>> e) throws Exception
                    {
                        CommentBeanDao dao = App.mSession.getCommentBeanDao();
                        List<CommentBean> list = dao.loadAll();
                        if (list == null || list.isEmpty())
                        {
                            String json = StringUtils.getJson(App.getContext(), "CommentBean.json");
                            Gson gson = new Gson();
                            list = gson.fromJson(json, new TypeToken<List<CommentBean>>()
                            {
                            }.getType());
                        }
                        e.onNext(list);
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)
                        .compose(this.<List<CommentBean>>loadTransformer())
                        .subscribeWith(new BaseObserver<List<CommentBean>>(mView)
                        {
                            @Override
                            public void onNext(List<CommentBean> list)
                            {
                                if (mView != null)
                                {
                                    mView.showData(list);
                                }
                            }
                        })
        );
    }

    /*获取收藏数据*/
    public void getCollectedData()
    {
        add(
                Flowable.create(new FlowableOnSubscribe<List<FoodBean>>()
                {
                    @Override
                    public void subscribe(FlowableEmitter<List<FoodBean>> e) throws Exception
                    {
                        FoodBeanDao dao = App.mSession.getFoodBeanDao();
                        List<FoodBean> list = dao.loadAll();
                        e.onNext(list != null ? list : new ArrayList<FoodBean>());
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)
                .compose(this.<List<FoodBean>>loadTransformer())
                .subscribeWith(new BaseObserver<List<FoodBean>>(mView)
                {
                    @Override
                    public void onNext(List<FoodBean> list)
                    {
                        if (mView != null)
                        {
                            mView.showData(list);
                        }
                    }
                })
        );
    }
}
