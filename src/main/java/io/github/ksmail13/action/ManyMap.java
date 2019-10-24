package io.github.ksmail13.action;

import lombok.AllArgsConstructor;
import org.reactivestreams.Subscriber;

import java.util.function.Function;

@AllArgsConstructor
class ManyMap<T, R> extends Many<R> {

    private Many<T> before;
    private Function<T, R> transform;

    @Override
    public void subscribe(Subscriber<? super R> s) {
        SubscriptionMap<T, R> trSubscriptionMap = new SubscriptionMap<>(transform, s);
        before.subscribe(trSubscriptionMap);
        s.onSubscribe(trSubscriptionMap);
    }
}