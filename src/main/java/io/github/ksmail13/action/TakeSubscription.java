package io.github.ksmail13.action;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicLong;

public class TakeSubscription<T> implements Subscription, Subscriber<T> {

    private Subscriber<? super T> subscriber;
    private Subscription subscription;

    private volatile AtomicLong cnt;
    private boolean complete;

    /**
     * cnt 만큼만 사용한다.
     * @param s
     * @param cnt
     */
    public TakeSubscription(Subscriber<? super T> s, long cnt) {
        this.subscriber = s;
        this.cnt = new AtomicLong(cnt);
    }

    @Override
    public void request(long n) {
        if (cnt.get() > 0 || !complete) {
            subscription.request(n);
        }
    }

    @Override
    public void cancel() {
        subscription.cancel();
        complete = true;
    }

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription = s;
    }

    @Override
    public void onNext(T t) {
        if (complete) {
            return;
        }

        if (cnt.getAndDecrement() > 0) {
            subscriber.onNext(t);
        }

        if (cnt.get() <= 0) {
            onComplete();
        }
    }

    @Override
    public void onError(Throwable t) {
        subscriber.onError(t);
    }

    @Override
    public void onComplete() {
        complete = true;
        subscriber.onComplete();
    }
}
