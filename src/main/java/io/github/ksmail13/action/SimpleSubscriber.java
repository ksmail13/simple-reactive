package io.github.ksmail13.action;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Consumer;

class SimpleSubscriber<T> implements Subscriber<T> {

    private final Consumer<T> next;
    private final Consumer<Throwable> error;
    private final Runnable complete;

    SimpleSubscriber(Consumer<T> next, Consumer<Throwable> error, Runnable complete) {
        this.next = next;
        this.error = error;
        this.complete = complete;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {
        next.accept(t);
    }

    @Override
    public void onError(Throwable t) {
        error.accept(t);
    }

    @Override
    public void onComplete() {
        complete.run();
    }
}
