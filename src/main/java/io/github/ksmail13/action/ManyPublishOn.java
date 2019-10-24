package io.github.ksmail13.action;

import io.github.ksmail13.schedule.Scheduler;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;

import java.util.concurrent.LinkedBlockingQueue;

@RequiredArgsConstructor
public class ManyPublishOn<T> extends Many<T> {

    private final Scheduler scheduler;

    @Override
    public void subscribe(Subscriber<? super T> s) {
        s.onSubscribe(LazySubscription.of(() -> new SubscriptionPublishOn<>(this, s, scheduler, LinkedBlockingQueue::new)));
    }
}
