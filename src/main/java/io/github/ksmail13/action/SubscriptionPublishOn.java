package io.github.ksmail13.action;

import io.github.ksmail13.schedule.Scheduler;
import io.github.ksmail13.schedule.Worker;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

class SubscriptionPublishOn<T> implements PollSubscription<T>, Subscriber<T>, Runnable {

    private final Many<T> before;
    private final Subscriber<? super T> actual;
    private final Supplier<BlockingQueue<T>> queueSupplier;
    private final Worker worker;

    private boolean poll = false;

    private Queue<T> q;
    private Subscription s;

    private volatile Throwable throwable;
    private static final AtomicReferenceFieldUpdater<SubscriptionPublishOn, Throwable> THROWABLE_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(SubscriptionPublishOn.class, Throwable.class, "throwable");

    private AtomicBoolean complete = new AtomicBoolean(false);

    public SubscriptionPublishOn(Many<T> before, Subscriber<? super T> actual, Scheduler scheduler, Supplier<BlockingQueue<T>> o) {
        this.before = before;
        this.actual = actual;
        this.queueSupplier = o;
        this.worker = scheduler.worker();
    }

    @Override
    public void request(long n) {
        if (s == null) {
            actual.onError(new NullPointerException("Subscript yet"));
            return;
        }
        s.request(n);
    }

    @Override
    public void cancel() {
        if (s == null) {
            actual.onError(new NullPointerException("Subscript yet"));
            return;
        }
        s.cancel();
    }

    @Override
    public void onSubscribe(Subscription s) {
        this.s = s;
        poll = s instanceof PollSubscription;
        if (poll) {
            q = queueSupplier.get();
        }
    }

    @Override
    public void onNext(T t) {
        if (!poll) {
            q.add(t);
        } else {
            worker.push(this);
        }
    }

    @Override
    public void onError(Throwable t) {
        THROWABLE_UPDATER.set(this, t);
    }

    @Override
    public void onComplete() {
        complete.set(true);
    }

    @Override
    public void run() {

    }

    @Override
    public T poll() {
        return q.poll();
    }
}
