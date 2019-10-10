package io.github.ksmail13.action;

import io.github.ksmail13.schedule.Scheduler;
import io.github.ksmail13.schedule.Worker;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@RequiredArgsConstructor
class SubscriptionSubscribeOn<T> implements Subscription, Subscriber<T> {
    private final Scheduler scheduler;
    private final Subscriber<T> next;
    private Subscription beforeSubscription;

    private Worker worker;

    @Override
    public void onSubscribe(Subscription s) {
        beforeSubscription = s;
    }

    @Override
    public void onNext(T t) {
        worker.push(() -> next.onNext(t));
    }

    @Override
    public void onError(Throwable t) {
        worker.push(() -> next.onError(t));
    }

    @Override
    public void onComplete() {
        worker.push(next::onComplete);
    }

    @Override
    public void request(long n) {
        worker = scheduler.worker();
        beforeSubscription.request(n);
    }

    @Override
    public void cancel() {
        beforeSubscription.cancel();
    }
}
