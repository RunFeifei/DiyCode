package com.dianrong.crnetwork.framework;

import android.os.Looper;
import android.support.annotation.NonNull;

import com.dianrong.crnetwork.dataformat.DrRoot;
import com.dianrong.crnetwork.dataformat.Entity;
import com.dianrong.crnetwork.error.ErrorCode;
import com.dianrong.crnetwork.framework.adapter.Call2ObservableAdapter;
import com.dianrong.crnetwork.framework.adapter.SchedulerAdapter;
import com.dianrong.crnetwork.framework.error.ErrorHandler;
import com.dianrong.crnetwork.framework.requests.Requests;
import com.dianrong.crnetwork.framework.subscriber.DefaultSubscriber;
import com.dianrong.crnetwork.framework.view.IBaseView;
import com.dianrong.crnetwork.response.DrResponse;
import com.dianrong.crnetwork.response.RequestException;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.ActivityLifecycleProvider;
import com.trello.rxlifecycle.components.FragmentLifecycleProvider;

import okhttp3.HttpUrl;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by PengFeifei on 17-4-20.
 */

public class ObservableHandler<T extends Entity> {

    private static final String TAG = ObservableHandler.class.getSimpleName();

    private IBaseView baseView;
    private FragmentLifecycleProvider fragmentLifecycle;
    private ActivityLifecycleProvider activityLifecycle;

    public ObservableHandler(IBaseView baseView, FragmentLifecycleProvider fragmentLifecycle) {
        this.baseView = baseView;
        this.fragmentLifecycle = fragmentLifecycle;
    }

    public ObservableHandler(IBaseView baseView, ActivityLifecycleProvider activityLifecycle) {
        this.baseView = baseView;
        this.activityLifecycle = activityLifecycle;
    }

    /***************************************Single Request Part***************************************/
    public void getContentData(Call<T> call, @NonNull Action1<Entity> action1) {
        getContentData(call, action1, null);
    }

    public void getContentData(Call<T> call, @NonNull Action1<Entity> onData, ErrorHandler errorHandler) {
        getContentObservable(call)
                .subscribe(new DefaultSubscriber<Entity>(baseView, call.request().url(), errorHandler) {
                    @Override
                    public void onHandleData(Entity entity) {
                        onData.call(entity);
                    }
                });
    }

    public Observable<Entity> getContentObservable(Call<T> call) {
        if (call == null || call.request() == null) {
            throw new RequestException(RequestException.ILLEGAL_URL, ErrorCode.REQUEST_NULL_ERR, "request is null");
        }
        HttpUrl url = call.request().url();
        Observable<T> observable = Call2ObservableAdapter.adapt(call, SchedulerAdapter.backScheduler);
        return observable
                .map(map2Content(url))
                .compose(bindUntilEvent())
                .observeOn(SchedulerAdapter.uiScheduler)
                .subscribeOn(SchedulerAdapter.backScheduler);
    }

    /***************************************Multiple Requests Part***************************************/
    public void getRequestsData(Requests<T> requests, @NonNull Action1<Entity> onData) {
        getRequestsData(requests, onData, null);
    }

    public void getRequestsData(Requests<T> requests, @NonNull Action1<Entity> onData, ErrorHandler errorHandler) {
        getRequestsObservable(requests)
                .subscribe(new DefaultSubscriber<Entity>(baseView, RequestException.REQUESTS_URL, errorHandler) {
                    @Override
                    public void onHandleData(Entity t) {
                        onData.call(t);
                    }
                });
    }

    public Observable<Entity> getRequestsObservable(Requests<T> requests) {
        checkMainThread();
        return Observable
                .create(new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        subscriber.onNext(requests.onRequests());
                        subscriber.onCompleted();
                    }
                })
                .map(map2Content(RequestException.REQUESTS_URL))
                .subscribeOn(SchedulerAdapter.backScheduler)
                .observeOn(SchedulerAdapter.uiScheduler);
    }


    /***************************************Utils Part***************************************/
    private void checkMainThread() {
        if (!Looper.getMainLooper().equals(Looper.myLooper())) {
            throw new IllegalStateException("Must be invoked in Main thread");
        }
    }

    /**
     * @return DrRoot mapTo DrRoot的content字段
     */
    private Func1<T, Entity> map2Content(HttpUrl httpUrl) {
        return new Func1<T, Entity>() {
            @Override
            public Entity call(T t) {
                if (t != null && t instanceof DrRoot) {
                    DrRoot root = (DrRoot) t;
                    if (DrResponse.checkRootData(root, httpUrl)) {
                        return ((DrRoot) t).getContent();
                    }
                }
                return t;
            }
        };
    }

    /**
     * 页面不可见自动解除订阅
     */
    private Observable.Transformer<Entity, Entity> bindUntilEvent() {
        if (fragmentLifecycle == null && activityLifecycle == null) {
            throw new IllegalStateException("bindUntilEvent params NPE");
        }

        if (fragmentLifecycle != null && activityLifecycle != null) {
            throw new IllegalStateException("fragment & activity both initialized");
        }
        if (activityLifecycle != null) {
            return activityLifecycle.bindUntilEvent(ActivityEvent.PAUSE);
        }
        return fragmentLifecycle.bindUntilEvent(FragmentEvent.PAUSE);
    }


}
