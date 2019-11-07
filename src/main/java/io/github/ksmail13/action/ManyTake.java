package io.github.ksmail13.action;

import lombok.AllArgsConstructor;
import org.reactivestreams.Subscriber;

@AllArgsConstructor
class ManyTake<T> extends Many<T> {
    private Many<T> source;
    private long cnt;

    @Override
    public void subscribe(Subscriber<? super T> s) {
        s.onSubscribe(LazySubscription.of(() -> {
            ManyTakeOperator<T> s1 = new ManyTakeOperator<>(s, cnt);
            source.subscribe(s1);
            return s1;
        }));
    }
}
