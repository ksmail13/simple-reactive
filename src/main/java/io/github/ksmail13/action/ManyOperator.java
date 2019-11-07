package io.github.ksmail13.action;

import lombok.extern.java.Log;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;

@Log
abstract class ManyOperator<T, R> implements Subscription, Subscriber<T> {

    private AtomicBoolean complete = new AtomicBoolean();
    private volatile Subscription subscription;
    static AtomicReferenceFieldUpdater<ManyOperator, Subscription> SUBSCRIPTION =
            AtomicReferenceFieldUpdater.newUpdater(ManyOperator.class, Subscription.class, "subscription");

    final Subscriber<? super R> subscriber;

    ManyOperator(Subscriber<? super R> subscriber) {
        this.subscriber = subscriber;
    }


    @Override
    public void onSubscribe(Subscription s) {
        if (!SUBSCRIPTION.compareAndSet(this, null, s)) {
            if (log.getLevel() == Level.FINE) {
                log.fine(() -> "resubscribe from " + this.getClass().getSimpleName());
            }
        }
    }

    @Override
    public void onComplete() {
        if(complete.compareAndSet(false, true)) {
            subscriber.onComplete();
        }
    }


    /**
     * default behavior of request in operator
     * @param n no of request elements
     */
    @Override
    public void request(long n) {
        Subscription subscription = SUBSCRIPTION.get(this);
        if (subscription instanceof LazySubscription) {
            SUBSCRIPTION.set(this, ((LazySubscription) subscription).getSubscription());
        }
        SUBSCRIPTION.get(this).request(n); // propagate to upstream
    }

    @Override
    public void cancel() {
        if (!isComplete()) {
            subscription.cancel(); // propagate to upstream
            onComplete(); // propagate to downstream
        }
    }

    boolean isComplete() {
        return complete.get();
    }
}
