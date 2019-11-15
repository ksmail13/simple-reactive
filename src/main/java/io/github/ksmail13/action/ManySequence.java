package io.github.ksmail13.action;

import io.github.ksmail13.util.SafeGetter;
import io.github.ksmail13.util.SafeSequenceGetter;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;

import java.util.Collection;

@RequiredArgsConstructor
public class ManySequence<T> extends Many<T> {
    private final SafeGetter<T> data;

    @Override
    public void subscribe(Subscriber<? super T> s) {
        s.onSubscribe(LazySubscription.of(() -> new SubscriptionSequence<>(data, s)));
    }
}
