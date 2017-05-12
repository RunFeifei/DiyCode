package com.example.root.okfit.CrRxbus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by PengFeifei on 17-5-12.
 * 参考:
 * http://www.jianshu.com/p/adfc72da6056
 * http://www.jianshu.com/p/ca090f6e2fe2
 */

public class CrObservable {

    private static volatile CrObservable crObservable;
    //Subject:both observable & subscriber
    private final Subject<Object, Object> bus;
    //stickk事件容器
    private final Map<Class<?>, CrBusEvent> stickyEventMap;

    private CrObservable() {
        //PublishSubject: 在订阅之后生效 先订阅 然后将Observable发送给观察者
        bus = new SerializedSubject<>(PublishSubject.create());
        stickyEventMap = new ConcurrentHashMap<>();
    }

    public static CrObservable getInstance() {
        if (crObservable == null) {
            synchronized (CrObservable.class) {
                if (crObservable == null) {
                    crObservable = new CrObservable();
                }
            }
        }
        return crObservable;
    }

    public void sendEvent(CrBusEvent event) {
        bus.onNext(event);
    }

    public void sendStickyEvent(CrBusEvent event) {
        synchronized (stickyEventMap) {
            stickyEventMap.put(event.getClass(), event);
        }
        bus.onNext(event);
    }

    public <T> Observable<T> getObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    public <T> Observable<T> getStickyObservable(Class<T> eventType) {
        synchronized (stickyEventMap) {
            Observable<T> observable = bus.ofType(eventType);
            final CrBusEvent event = stickyEventMap.get(eventType);

            if (event != null) {
                return observable.mergeWith(Observable.just(eventType.cast(event)));
            } else {
                return observable;
            }
        }
    }

    public <T> T getStickyEvent(Class<T> eventType, @CrBusEvent.EventId int evenId) {
        synchronized (stickyEventMap) {
            return eventType.cast(stickyEventMap.get(evenId));
        }
    }

    public <T> T removeStickyEvent(Class<T> eventType, @CrBusEvent.EventId int evenId) {
        synchronized (stickyEventMap) {
            return eventType.cast(stickyEventMap.remove(evenId));
        }
    }

    public void removeAllStickyEvents() {
        synchronized (stickyEventMap) {
            stickyEventMap.clear();
        }
    }


}
