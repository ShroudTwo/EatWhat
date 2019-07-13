package com.wei.cookbook.net;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class BasePresenter<V extends BaseView>
{
    protected V mView;
    private Reference<V> mVReference;
    protected CompositeDisposable mComposite;
    private LifecycleProvider<ActivityEvent> mProvider;


    public BasePresenter(V view, LifecycleProvider<ActivityEvent> provider)
    {
        attchView(view);
        this.mProvider = provider;
    }


    private void attchView(V view)
    {
        mVReference = new WeakReference<V>(view);
        mView = mVReference.get();
        mComposite = new CompositeDisposable();
    }


    /*添加进度条*/
    public <T> FlowableTransformer<T, T> loadTransformer()
    {
        return new FlowableTransformer<T, T>()
        {
            @Override
            public Publisher<T> apply(Flowable<T> flowable)
            {
                return flowable.subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Subscription>()
                        {
                            @Override
                            public void accept(Subscription subscription) throws Exception
                            {
                                if (mView != null)
                                {
                                    mView.showLoading();
                                }
                            }
                        }).doFinally(new Action()
                        {
                            @Override
                            public void run() throws Exception
                            {
                                if (mView != null)
                                {
                                    mView.dissLoading();
                                }
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                        .compose(mProvider.<T>bindToLifecycle());
            }
        };
    }


    public void detachView()
    {
        if (mView != null && mVReference != null)
        {
            mVReference.clear();
            mVReference = null;
        }
    }

    protected void add(Disposable disposable)
    {
        if (!mComposite.isDisposed())
        {
            mComposite.add(disposable);
        }
    }

    public void cancle(Disposable disposable)
    {
        if (!mComposite.isDisposed())
        {
            mComposite.remove(disposable);
        }
    }

    public void detach()
    {
        detachView();
        if (!mComposite.isDisposed())
        {
            mComposite.clear();
        }
    }
}
