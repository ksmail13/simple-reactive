package io.github.ksmail13.action;

import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

/**
 * initialize subscription lazily
 */
public class LazySubscription implements Subscription {

    private final Supplier<Subscription> subscriptionSupplier;
    private volatile Subscription subscription;

    private static final AtomicReferenceFieldUpdater<LazySubscription, Subscription> SUBSCRIPTION =
            AtomicReferenceFieldUpdater.newUpdater(LazySubscription.class, Subscription.class, "subscription");

    public static LazySubscription of(Supplier<Subscription> subscriptionSupplier) {
        return new LazySubscription(subscriptionSupplier);
    }

    private LazySubscription(Supplier<Subscription> subscriptionSupplier) {
        this.subscriptionSupplier = subscriptionSupplier;
    }

    @Override
    public void request(long n) {
        setSubscription();
        subscription.request(n);
    }

    @Override
    public void cancel() {
        setSubscription();
        subscription.cancel();
    }

    /**
     * 기존에 생성된 subscription이 없을 경우
     * 새로 생성하여 필드에 바인딩한다.
     */
    private void setSubscription() {
        if (SUBSCRIPTION.get(this) == null) {
            SUBSCRIPTION.set(this, subscriptionSupplier.get());
        }
    }

    /**
     * 생성된 subscription을 리턴
     * 없을 경우 새로 생성
     * @return 내부 subscription
     */
    public Subscription getSubscription() {
        setSubscription();
        return SUBSCRIPTION.get(this);
    }
}
