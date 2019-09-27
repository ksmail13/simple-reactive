package io.github.ksmail13.action;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
class TakeManySubscription<T> implements Subscription, Subscriber<T> {

    private Many<T> before;
    private volatile AtomicInteger cnt;
    private final Subscriber<? super T> subscriber;
    private volatile boolean finish;
    private Subscription subscription;

    public TakeManySubscription(Many<T> before, int cnt, Subscriber<? super T> subscriber) {
        this.before = before;
        this.cnt = new AtomicInteger(cnt);
        this.subscriber = subscriber;
    }

    @Override
    public void request(long n) {
        if (finish) {
            return;
        }

        log.trace("request {} datas", n);
        before.subscribe(this);
        subscription.request(Math.min(n, cnt.get()));
    }

    @Override
    public void cancel() {
        if (finish) {
            return;
        }

        subscription.cancel();
        onComplete();
    }

    @Override
    public void onSubscribe(Subscription s) {
        subscription = s;
    }

    @Override
    public void onNext(T t) {
        if (finish) {
            return;
        }

        log.trace("OnNext({})", t);
        subscriber.onNext(t);
        if (cnt.decrementAndGet() == 0) {
            subscription.cancel();
            onComplete();
        }
    }

    @Override
    public void onError(Throwable t) {
        if (finish) {
            return;
        }

        subscriber.onError(t);
        finish = true;
    }

    @Override
    public void onComplete() {
        if (finish) {
            return;
        }

        finish = true;
        subscriber.onComplete();
    }
}
