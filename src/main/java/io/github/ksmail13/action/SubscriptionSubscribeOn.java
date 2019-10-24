package io.github.ksmail13.action;

import io.github.ksmail13.schedule.Scheduler;
import io.github.ksmail13.schedule.Worker;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

class SubscriptionSubscribeOn<T> implements Subscription, Subscriber<T> {
    private final Subscriber<T> next;
    private Subscription beforeSubscription;

    private Worker worker;

    public SubscriptionSubscribeOn(Scheduler scheduler, Subscriber<T> next) {
        this.next = next;
        this.worker = scheduler.worker();
    }

    @Override
    public void onSubscribe(Subscription s) {
        beforeSubscription = s;
    }

    @Override
    public void onNext(T t) {
        next.onNext(t);
    }

    @Override
    public void onError(Throwable t) {
        next.onError(t);
    }

    @Override
    public void onComplete() {
        next.onComplete();
    }

    @Override
    public void request(long n) {
        worker.push(() -> beforeSubscription.request(n));
    }

    @Override
    public void cancel() {
        beforeSubscription.cancel();
    }
}
