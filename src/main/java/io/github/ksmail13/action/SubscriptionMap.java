package io.github.ksmail13.action;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Function;

@RequiredArgsConstructor
public class SubscriptionMap<T, R> implements Subscription, Subscriber<T> {

    private final Function<T, R> transform;
    private final Subscriber<? super R> nextSubscriber;
    private Subscription beforeSubscription;

    @Override
    public void request(long n) {
        beforeSubscription.request(n);
    }

    @Override
    public void cancel() {
        beforeSubscription.cancel();
    }

    @Override
    public void onSubscribe(Subscription s) {
        beforeSubscription = s;
    }

    @Override
    public void onNext(T t) {
        try {
            nextSubscriber.onNext(transform.apply(t));
        } catch (Exception e) {
            onError(e);
        }
    }

    @Override
    public void onError(Throwable t) {
        nextSubscriber.onError(t);
    }

    @Override
    public void onComplete() {
        nextSubscriber.onComplete();
    }
}
