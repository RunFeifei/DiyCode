package com.example.root.okfit.CrRxbus;

import android.support.annotation.NonNull;
import android.util.Log;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.ActivityLifecycleProvider;
import com.trello.rxlifecycle.components.FragmentLifecycleProvider;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by PengFeifei on 17-5-12.
 */

public class BusSubscriber {

    private FragmentLifecycleProvider fragmentLifecycleProvider;
    private ActivityLifecycleProvider activityLifecycleProvider;

    private int eventId;
    private Action1<BusEvent> onNext;
    private Action1<Throwable> onError;

    private BusSubscriber(@NonNull FragmentLifecycleProvider provider) {
        this.fragmentLifecycleProvider = provider;
    }

    private BusSubscriber(@NonNull ActivityLifecycleProvider provider) {
        this.activityLifecycleProvider = provider;
    }

    public static BusSubscriber bind(@NonNull FragmentLifecycleProvider provider) {
        return new BusSubscriber(provider);
    }

    public static BusSubscriber bind(@NonNull ActivityLifecycleProvider provider) {
        return new BusSubscriber(provider);
    }


    public BusSubscriber bindEvent(int eventId) {
        this.eventId = eventId;
        return this;
    }

    /**
     * required
     */
    public BusSubscriber onNext(Action1<BusEvent> action) {
        this.onNext = action;
        return this;
    }

    /**
     * optional
     */
    public BusSubscriber onError(Action1<Throwable> action) {
        this.onError = action;
        return this;
    }

    public Subscription create() {
        return create(false);
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

        rx.Observable observable = isSticky ?
                BusObservable.bind().getStickyObservable(BusEvent.class)
                : BusObservable.bind().getObservable(BusEvent.class);
        observable = observable.compose(fragmentLifecycleProvider != null
                ? fragmentLifecycleProvider.<BusEvent>bindUntilEvent(FragmentEvent.DESTROY_VIEW) :
                activityLifecycleProvider.<BusEvent>bindUntilEvent(ActivityEvent.DESTROY));

        //return CrObservable.bind().getObservable(CrBusEvent.class)
        //compose()将生命周期包装成了Observable
        return observable.filter(new Func1<BusEvent, Boolean>() {
            @Override
            public Boolean call(BusEvent events) {
                //根据Id进行过滤
                Log.d("Rxbus-->", "filter");
                return events.getEventId() == eventId;
            }
        })
                .subscribe(new rx.Subscriber<BusEvent>() {
                    @Override
                    public void onCompleted() {
                        Log.d("Rxbus-->", "onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (onError != null) {
                            onError.call(e);
                        }
                        Log.d("Rxbus-->", e.toString());
                    }

                    @Override
                    public void onNext(BusEvent busEvent) {
                        try {
                            onNext.call(busEvent);
                        } catch (Exception e) {
                            if (onError != null) {
                                onError.call(e);
                            }
                            Log.d("Rxbus-->", e.toString());
                        }
                    }
                });
    }
}