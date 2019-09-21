package io.github.ksmail13.action;

import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

public class LazySubscription implements Subscription {

    private final Supplier<Subscription> subscriptionSupplier;
    private volatile Subscription subscriber;

    private static final AtomicReferenceFieldUpdater<LazySubscription, Subscription> UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(LazySubscription.class, Subscription.class, "subscriber");

    public static LazySubscription of(Supplier<Subscription> subscriptionSupplier) {
        return new LazySubscription(subscriptionSupplier);
    }

    private LazySubscription(Supplier<Subscription> subscriptionSupplier) {
        this.subscriptionSupplier = subscriptionSupplier;
    }

    @Override
    public void request(long n) {
        setSubscription();
        subscriber.request(n);
    }

    @Override
    public void cancel() {
        setSubscription();
        subscriber.cancel();
    }

    private void setSubscription() {
        if (UPDATER.get(this) == null) {
            UPDATER.set(this, subscriptionSupplier.get());
        }
    }
}
