package io.github.ksmail13.action;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Take operator for {@link Many}
 * @param <T> data type
 */
class ManyTakeOperator<T> extends ManyOperator<T, T> {

    private Subscription subscription;

    private volatile AtomicLong cnt;

    /**
     * take {@code cnt} items
     *
     * @param s downstream
     * @param cnt take count
     */
    ManyTakeOperator(Subscriber<? super T> s, long cnt) {
        super(s);
        this.cnt = new AtomicLong(cnt);

    }

    @Override
    public void request(long n) {
        if (cnt.get() > 0 || !isComplete()) {
            super.request(n);
        }
    }

    @Override
    public void onNext(T t) {
        if (isComplete()) {
            return;
        }

        if (cnt.getAndDecrement() > 0) {
            subscriber.onNext(t);
        }

        if (cnt.get() == 0) {
            onComplete();
        }
    }

    @Override
    public void onError(Throwable t) {
        subscriber.onError(t);
    }

}
