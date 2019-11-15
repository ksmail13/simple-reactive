package io.github.ksmail13.action;

import io.github.ksmail13.schedule.Scheduler;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;

import java.util.concurrent.LinkedBlockingQueue;

@RequiredArgsConstructor
public class ManyPublishOn<T> extends Many<T> {

    private final Many<T> before;
    private final Scheduler scheduler;

    @Override
    public void subscribe(Subscriber<? super T> s) {
        s.onSubscribe(LazySubscription.of(() -> {
            SubscriptionPublishOn<? super T> publishOn = new SubscriptionPublishOn<>(s, scheduler, LinkedBlockingQueue::new);
            before.subscribe(publishOn);
            return publishOn;
        }));
    }
}
