package com.example.root.okfit.CrRxbus;

import android.support.annotation.NonNull;
import android.util.Log;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.ActivityLifecycleProvider;
import com.trello.rxlifecycle.components.FragmentLifecycleProvider;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by PengFeifei on 17-5-12.
 */

public class CrSubscriber {

    private FragmentLifecycleProvider fragmentLifecycleProvider;
    private ActivityLifecycleProvider activityLifecycleProvider;

    private CrBusEvent crBusEvent;
    private Action1<CrBusEvent> onNext;
    private Action1<Throwable> onError;

    private CrSubscriber(@NonNull FragmentLifecycleProvider provider) {
        this.fragmentLifecycleProvider = provider;
    }

    private CrSubscriber(@NonNull ActivityLifecycleProvider provider) {
        this.activityLifecycleProvider = provider;
    }

    public static CrSubscriber getFragmentSubscriber(@NonNull FragmentLifecycleProvider provider) {
        return new CrSubscriber(provider);
    }

    public static CrSubscriber getActivitySubscriber(@NonNull ActivityLifecycleProvider provider) {
        return new CrSubscriber(provider);
    }


    public CrSubscriber bindEvent(CrBusEvent event) {
        this.crBusEvent = event;
        return this;
    }

    /**
     * required
     */
    public CrSubscriber onNext(Action1<CrBusEvent> action) {
        this.onNext = action;
        return this;
    }

    /**
     * optional
     */
    public CrSubscriber onError(Action1<Throwable> action) {
        this.onError = action;
        return this;
    }

    /**
     * @param isSticky 是否是Sticky事件
     *                 Sticky事件:先发事件,后订阅,订阅时也可以收到该事件
     */
    public Subscription create(boolean isSticky) {
        if (fragmentLifecycleProvider == null && activityLifecycleProvider == null) {
            throw new IllegalStateException("constructor params NPE");
        }

        if (fragmentLifecycleProvider != null && activityLifecycleProvider != null) {
            throw new IllegalStateException("fragment & activity both do subscribed????");
        }

        Observable observable = isSticky ?
                CrObservable.getInstance().getStickyObservable(CrBusEvent.class)
                : CrObservable.getInstance().getObservable(CrBusEvent.class);
        observable= observable.compose(fragmentLifecycleProvider != null
                ? fragmentLifecycleProvider.<CrBusEvent>bindUntilEvent(FragmentEvent.DESTROY_VIEW) :
                activityLifecycleProvider.<CrBusEvent>bindUntilEvent(ActivityEvent.DESTROY));

        //return CrObservable.getInstance().getObservable(CrBusEvent.class)
        //compose()将生命周期包装成了Observable
        return observable.filter(new Func1<CrBusEvent, Boolean>() {
                    @Override
                    public Boolean call(CrBusEvent events) {
                        //根据Id进行过滤
//                        return events.getEventId() == crBusEvent.getEventId();
                        return true;
                    }
                })
                .subscribe(new rx.Subscriber<CrBusEvent>() {
                    @Override
                    public void onCompleted() {
                        Log.e("CrRxbus-->", "onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (onError != null) {
                            onError.call(e);
                        }
                        Log.e("CrRxbus-->", e.toString());
                    }

                    @Override
                    public void onNext(CrBusEvent crBusEvent) {
                        try {
                            onNext.call(crBusEvent);
                        } catch (Exception e) {
                            if (onError != null) {
                                onError.call(e);
                            }
                            Log.e("CrRxbus-->", e.toString());
                        }
                    }
                });
    }
}