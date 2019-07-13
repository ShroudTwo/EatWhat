package com.wei.cookbook.utils;

import com.wei.cookbook.model.BaseBean;
import com.wei.cookbook.model.BaseResultBean;
import com.wei.cookbook.net.ApiException;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class RxUtils
{

    /*调整线程*/
    public static <T> FlowableTransformer<T, T> rxFlowableSchedulers()
    {
        return new FlowableTransformer<T, T>()
        {
            @Override
            public Publisher<T> apply(Flowable<T> flowable)
            {
                return flowable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /*调整线程*/
    public static <T> ObservableTransformer<T, T> rxObservableSchedlers()
    {
        return new ObservableTransformer<T, T>()
        {
            @Override
            public ObservableSource<T> apply(Observable<T> observable)
            {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /*对请求后的数据进行处理*/
    public static <T> FlowableTransformer<BaseBean<T>, T> handleBaseData()
    {
        return new FlowableTransformer<BaseBean<T>, T>()
        {
            @Override
            public Publisher<T> apply(Flowable<BaseBean<T>> flowable)
            {
                return flowable.flatMap(new Function<BaseBean<T>, Publisher<T>>()
                {
                    @Override
                    public Publisher<T> apply(final BaseBean<T> data) throws Exception
                    {
                        int code = data.getCode();
                        if (code == 200 || code == 201)
                        {
                            return Flowable.create(new FlowableOnSubscribe<T>()
                            {
                                @Override
                                public void subscribe(FlowableEmitter<T> e) throws Exception
                                {
                                    e.onNext(data.getT() == null ? ((T) data.getMsg()) : data.getT());
                                    e.onComplete();
                                }
                            }, BackpressureStrategy.BUFFER);
                        } else
                        {
                            return Flowable.error(new ApiException(data.getMsg(), data.getCode()));
                        }
                    }
                });
            }
        };
    }


    /*对请求后的数据进行处理*/
    public static <T> FlowableTransformer<BaseResultBean<T>, T> handleBaseResult()
    {
        return new FlowableTransformer<BaseResultBean<T>, T>()
        {
            @Override
            public Publisher<T> apply(Flowable<BaseResultBean<T>> flowable)
            {
                return flowable.flatMap(new Function<BaseResultBean<T>, Publisher<T>>()
                {
                    @Override
                    public Publisher<T> apply(final BaseResultBean<T> data) throws Exception
                    {
                        int code = data.getCode();
                        if (code == 200 || code == 201)
                        {
                            return Flowable.create(new FlowableOnSubscribe<T>()
                            {
                                @Override
                                public void subscribe(FlowableEmitter<T> e) throws Exception
                                {
                                    e.onNext(data.getData().getT() == null ? ((T) data.getMsg()) : data.getData().getT());
                                    e.onComplete();
                                }
                            }, BackpressureStrategy.BUFFER);
                        } else
                        {
                            return Flowable.error(new ApiException(data.getMsg(), data.getCode()));
                        }
                    }
                });
            }
        };
    }


}
