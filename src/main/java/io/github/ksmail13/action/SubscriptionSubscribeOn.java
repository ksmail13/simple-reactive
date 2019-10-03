package io.github.ksmail13.action;

import io.github.ksmail13.schedule.Scheduler;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@RequiredArgsConstructor
class SubscriptionSubscribeOn<T> implements Subscription, Subscriber<T> {
    private final Scheduler scheduler;
    private final Subscriber<T> next;
    private Subscription beforeSubscription;

    @Override
    public void onSubscribe(Subscription s) {
        beforeSubscription = s;
    }

    @Override
    public void onNext(T t) {
        scheduler.work(() -> next.onNext(t));
    }

    @Override
    public void onError(Throwable t) {
        scheduler.work(() -> next.onError(t));
    }

    @Override
    public void onComplete() {
        scheduler.work(next::onComplete);
    }

    @Override
    public void request(long n) {
        beforeSubscription.request(n);
    }

    @Override
    public void cancel() {
        beforeSubscription.cancel();
    }
}
