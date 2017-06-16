package com.dianrong.crnetwork.framework.adapter;

import com.dianrong.crnetwork.response.RequestException;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by PengFeifei on 17-6-15.
 */

public class Call2ObservableAdapter {

    public static <R> Observable<R> adapt(Call<R> call) {
        return adapt(call, null);
    }

    public static <R> Observable<R> adapt(Call<R> call, Scheduler scheduler) {
        Observable<R> observable =
                Observable
                        .create(new CallOnSubscribe<>(call))
                        .flatMap(new Func1<Response<R>, Observable<R>>() {
                            @Override
                            public Observable<R> call(Response<R> response) {
                                if (response.isSuccessful()) {
                                    return Observable.just(response.body());
                                }
                                return Observable.error(new RequestException(call.request().url(), response.code(), response.toString()));
                            }
                        });
        if (scheduler != null) {
            return observable.subscribeOn(scheduler);
        }
        return observable.subscribeOn(Schedulers.io());
    }

    private static final class CallOnSubscribe<T> implements Observable.OnSubscribe<Response<T>> {
        private final Call<T> originalCall;

        CallOnSubscribe(Call<T> originalCall) {
            this.originalCall = originalCall;
        }

        @Override
        public void call(final Subscriber<? super Response<T>> subscriber) {
            // Since Call is a one-shot type, clone it for each new subscriber.
            final Call<T> call = originalCall.clone();

            // Attempt to cancel the call if it is still in-flight on unsubscription.
            //subscribe 增加一个 unsubscribe 的事件,请求完成的时候，会自动对 call 进行一个终止
            //http://m.blog.csdn.net/Card361401376/article/details/51077620
            subscriber.add(Subscriptions.create(new Action0() {
                @Override
                public void call() {
                    call.cancel();
                }
            }));

            try {
                Response<T> response = call.execute();
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(response);
                }
            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(t);
                }
                return;
            }

            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        }
    }
}
