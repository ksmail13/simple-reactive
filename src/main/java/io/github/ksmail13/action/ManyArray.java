package io.github.ksmail13.action;

import io.github.ksmail13.util.SafeArrayGetter;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;

@RequiredArgsConstructor
class ManyArray<T> extends Many<T> {
    private final T[] data;

    @Override
    public void subscribe(Subscriber<? super T> s) {
        s.onSubscribe(LazySubscription.of(() -> new SubscriptionSequence<>(new SafeArrayGetter<>(data), s)));
    }
}
