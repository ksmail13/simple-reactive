package io.github.ksmail13.action;

import io.github.ksmail13.schedule.Scheduler;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;

@RequiredArgsConstructor
class ManySubscribeOn<T> extends Many<T> {
    private final Many<T> before;
    private final Scheduler scheduler;

    @Override
    public void subscribe(Subscriber<? super T> s) {
        s.onSubscribe(LazySubscription.of(() -> {
            SubscriptionSubscribeOn<? super T> s1 = new SubscriptionSubscribeOn<>(scheduler, s);
            before.subscribe(s1);
            return s1;
        }));
    }
}
