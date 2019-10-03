package io.github.ksmail13.action;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;

@RequiredArgsConstructor
class ManyJust<T> extends Many<T> {
    private final T data;

    @Override
    public void subscribe(Subscriber<? super T> s) {
        s.onSubscribe(LazySubscription.of(() -> new SubscriptionOne<>(data, s)));
    }
}
