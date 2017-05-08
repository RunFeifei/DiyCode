package com.example.root.okfit.uibinder;

import android.os.Looper;

import com.example.crnetwork.error.ErrorCode;
import com.example.crnetwork.response.RequestException;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.HttpUrl;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Lei Guoting on 17-3-9.
 * Core:将任务抽象成接口的实例,在合适的时候通过该实例进行调用
 */
public final class UiBinderAgent {

    private UiBinderView view;

    private UiBinderAgent() {/*hide*/}

    public static UiBinderAgent newInstance() {
        return new UiBinderAgent();
    }

    public <T> UiBinder<T> bornUiBinder() {
        return new SimpleUiBinder<T>(this.view);
    }

    public UiBinderBatch bornUiBinderBatch() {
        return new SimpleUiBinderBatch(this.view);
    }

    /**
     * 在父Activity Fragment中把当前对象把Activity fragment实例传进来
     * 并实现了UiBinderView的三个事件 onUiBinderStat...
     *
     * @param view
     */
    public void setUiBinderView(UiBinderView view) {
        this.view = view;
    }

    // NOTE: 17-5-5 泛型T代表返回的数据格式
    private static class SimpleUiBinder<T> implements UiBinder<T> {
        private Work mWork;
        private Error mError;
        private Hold mHold;

        private int type;
        private UiBinderView view;
        private Runnable precededTask;
        private Runnable completedTask;

        private AtomicInteger batchCount;
        private AtomicBoolean isBatchStart;

        SimpleUiBinder(UiBinderView view) {
            this.view = view;
        }

        SimpleUiBinder(UiBinderView view, AtomicInteger batchCount) {
            this.view = view;
            this.batchCount = batchCount;
            this.isBatchStart = new AtomicBoolean(false);
        }

        @Override
        public UiBinder precedeInUi(Runnable precededTask) {
            this.precededTask = precededTask;
            return this;
        }

        @Override
        public UiBinder workInBackground(Work<T> work) {
            if (work == null) {
                throw new NullPointerException();
            }
            this.mWork = work;
            return this;
        }

        @Override
        public UiBinder catchErrorInUi(Error error) {
            this.mError = error;
            return this;
        }

        /**
         * 将后续的处理事件传递进来
         */
        @Override
        public UiBinder holdDataInUi(Hold<T> hold) {
            this.mHold = hold;
            return this;
        }

        @Override
        public UiBinder completeInUi(Runnable completedTask) {
            this.completedTask = completedTask;
            return this;
        }

        /**
         * 将请求任务Work 和 后续处理任务Hold传进来后
         * 进行实施
         */
        @Override
        public UiBinder apply() {
            if (batchCount == null) {
                return applyInner(UiBinder.BEHAVIOR_LOADING_NORMAL);
            }
            return this;
        }

        /**
         * 将请求任务Work 和 后续处理任务Hold传进来后
         * 进行实施
         */
        @Override
        public UiBinder applySilence() {
            if (batchCount == null) {
                return applyInner(UiBinder.BEHAVIOR_SILENCE);
            }
            return this;
        }

        /**
         * 将请求任务Work 和 后续处理任务Hold传进来后
         * 进行实施
         */
        @Override
        public UiBinder apply(int behavior) {
            if (batchCount == null) {
                return applyInner(behavior);
            }
            return this;
        }

        /**
         * 真正进行实施的方法
         */
        private UiBinder applyInner(int behavior) {
            //必须在UI线程将此指令发出,不允许线程中新开线程
            if (!Looper.getMainLooper().equals(Looper.myLooper())) {
                throw new IllegalStateException("Must be invoked in Main thread");
            }

            this.type = behavior;
            final Work<T> work = this.mWork;
            final Hold<T> hold = this.mHold;

            AtomicBoolean isBatchStart = this.isBatchStart;
            if (BEHAVIOR_SILENCE != behavior && (isBatchStart == null || !isBatchStart.get())) {
                //非静态模式
                if (isBatchStart != null) {
                    isBatchStart.set(true);
                }
                UiBinderView view = this.view;
                if (view != null) {
                    //显示loading dialog
                    view.onUiBinderStart(behavior);
                }
            }

            // NOTE: 17-4-27 precededTask没有进行同步?
            Runnable precededTask = this.precededTask;
            if (precededTask != null) {
                precededTask.run();
            }

            Observable.create(new Observable.OnSubscribe<T>() {
                @Override
                public void call(Subscriber<? super T> subscriber) {
                    subscriber.onNext(work.onWork());
                    subscriber.onCompleted();
                }
            })
                    .subscribeOn(SingletonScheduler.backScheduler)
                    .observeOn(SingletonScheduler.uiScheduler)
                    .subscribe(new Subscriber<T>() {
                        @Override
                        public void onCompleted() {
                            SimpleUiBinder binder = SimpleUiBinder.this;
                            Runnable completedTask = binder.completedTask;
                            if (completedTask != null) {
                                completedTask.run();
                            }

                            int type = binder.type;
                            UiBinderView view = binder.view;
                            AtomicInteger batchCount = binder.batchCount;
                            if (view != null &&
                                    BEHAVIOR_SILENCE != type
                                    && (batchCount == null || batchCount.decrementAndGet() == 0)) {
                                view.onUiBinderEnd(type);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            try {
                                Error error = SimpleUiBinder.this.mError;
                                boolean isdefinedException = e instanceof RequestException;
                                RequestException requestException;
                                if (isdefinedException) {
                                    requestException = (RequestException) e;
                                } else {
                                    requestException = new RequestException(HttpUrl.parse("https://www.null.com/"), ErrorCode.UNKNOWN_ERR, e);
                                }
                                /**
                                 * 在此 catchErrorInUi()
                                 */
                                boolean isCatched = isdefinedException
                                        && error != null
                                        && error.onError((RequestException) e);

                                /**
                                 * catchErrorInUi()-->没有拦截or不是已经定义过的Exception则传递到onUiBinderError()中
                                 */
                                UiBinderView view = SimpleUiBinder.this.view;
                                if (view != null && !isCatched) {
                                    view.onUiBinderError(requestException);
                                }
                            } catch (Exception e1) {
                                view.onUiBinderError(new RequestException(HttpUrl.parse("https://www.null.com/"), ErrorCode.UNKNOWN_ERR, e));
                            } finally {
                                onCompleted();
                            }
                        }

                        /**
                         * 在此 holdDataInUi()
                         */
                        @Override
                        public void onNext(T data) {
                            Hold<T> h = hold;
                            if (h != null) {
                                h.onResultHold(data);
                            }

                        }
                    });


            return this;
        }
    }

    private static class SingletonScheduler {
        static final Scheduler backScheduler = Schedulers.from(ExecutorManager.get().getBackExecutor());
        static final Scheduler uiScheduler = AndroidSchedulers.mainThread();
    }

    private static class SimpleUiBinderBatch implements UiBinderBatch {
        private UiBinderView view;
        private AtomicInteger batchCount = new AtomicInteger();

        private ArrayList<SimpleUiBinder> binderList = new ArrayList<>(5);

        SimpleUiBinderBatch(UiBinderView view) {
            this.view = view;
        }

        @Override
        public <T> UiBinder<T> born() {
            batchCount.incrementAndGet();
            SimpleUiBinder<T> binder = new SimpleUiBinder<>(this.view, batchCount);
            binderList.add(binder);
            return binder;
        }

        @Override
        public void apply() {
            apply(UiBinder.BEHAVIOR_LOADING_NORMAL);
        }

        @Override
        public void apply(int type) {
            ArrayList<SimpleUiBinder> list = this.binderList;
            int size = list.size();
            if (size == 0) {
                return;
            }

            for (int i = 0; i < size; i++) {
                list.get(i).applyInner(type);
            }
        }
    }
}
