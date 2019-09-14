package io.github.ksmail13.action;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@RequiredArgsConstructor
class SubscriptionOne<T> implements Subscription {

    @NonNull
    private final T data;
    private final Subscriber<? super T> subscriber;

    private volatile boolean stopped;

    @Override
    public void request(long n) {
        if (stopped) {
            return;
        }

        subscriber.onNext(data);
        subscriber.onComplete();
    }

    @Override
    public void cancel() {
        stopped = true;
        subscriber.onComplete();
    }
}
