package com.example.root.okfit.CrRxbus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by PengFeifei on 17-5-12.
 * 参考:
 * http://www.jianshu.com/p/adfc72da6056
 * http://www.jianshu.com/p/ca090f6e2fe2
 */

public class BusObservable {

    private static volatile BusObservable busObservable;
    //Subject:both observable & subscriber
    private final Subject<Object, Object> bus;
    //stickk事件容器
    private final Map<Class<?>, BusEvent> stickyEventMap;

    private BusObservable() {
        //PublishSubject: 在订阅之后生效 先订阅 然后将Observable发送给观察者
        bus = new SerializedSubject<>(PublishSubject.create());
        stickyEventMap = new ConcurrentHashMap<>();
    }

    public static BusObservable bind() {
        if (busObservable == null) {
            synchronized (BusObservable.class) {
                if (busObservable == null) {
                    busObservable = new BusObservable();
                }
            }
        }
        return busObservable;
    }

    public void sendEvent(BusEvent event) {
        bus.onNext(event);
    }

    public void sendStickyEvent(BusEvent event) {
        synchronized (stickyEventMap) {
            stickyEventMap.put(event.getClass(), event);
        }
        bus.onNext(event);
    }

    public <T> rx.Observable getObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    public <T> rx.Observable getStickyObservable(Class<T> eventType) {
        synchronized (stickyEventMap) {
            rx.Observable observable = bus.ofType(eventType);
            final BusEvent event = stickyEventMap.get(eventType);

            if (event != null) {
                return observable.mergeWith(rx.Observable.just(eventType.cast(event)));
            } else {
                return observable;
            }
        }
    }

    public <T> T getStickyEvent(Class<T> eventType, int evenId) {
        synchronized (stickyEventMap) {
            return eventType.cast(stickyEventMap.get(evenId));
        }
    }

    public <T> T removeStickyEvent(Class<T> eventType, int evenId) {
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
