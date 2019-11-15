package io.github.ksmail13.action;

import org.reactivestreams.Subscriber;

import java.util.function.Function;

public class ManyMapOperator<T, R> extends ManyOperator<T, R> {

    private final Function<T, R> transform;

    ManyMapOperator(Function<T, R> transform, Subscriber<? super R> subscriber) {
        super(subscriber);
        this.transform = transform;
    }

    @Override
    public void onNext(T t) {
        try {
            subscriber.onNext(transform.apply(t));
        } catch (Exception e) {
            onError(e);
        }
    }

    @Override
    public void onError(Throwable t) {
        subscriber.onError(t);
    }

    @Override
    public void onComplete() {
        subscriber.onComplete();
    }
}
