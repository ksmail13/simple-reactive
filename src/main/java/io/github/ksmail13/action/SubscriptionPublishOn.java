package io.github.ksmail13.action;

import io.github.ksmail13.schedule.Scheduler;
import io.github.ksmail13.schedule.Worker;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

class SubscriptionPublishOn<T> extends QueueSubscription<T> implements Subscriber<T>, Runnable {
    private final Subscriber<? super T> actual;
    private final Supplier<BlockingQueue<T>> queueSupplier;
    private final Worker worker;

    private volatile boolean poll = false;
    private volatile AtomicBoolean running = new AtomicBoolean();

    private Queue<T> q;
    private Subscription s;

    private volatile Throwable throwable;
    private static final AtomicReferenceFieldUpdater<SubscriptionPublishOn, Throwable> THROWABLE_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(SubscriptionPublishOn.class, Throwable.class, "throwable");

    private AtomicBoolean complete = new AtomicBoolean(false);

    public SubscriptionPublishOn(Subscriber<? super T> actual, Scheduler scheduler, Supplier<BlockingQueue<T>> o) {
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
        poll = s instanceof QueueSubscription;
        if (!poll) {
            q = queueSupplier.get();
        } else {
            q = this.s;
        }
    }

    @Override
    public void onNext(T t) {
        if (!poll) {
            q.add(t);
        }

        if (!running.get()) {
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
        running.set(true);
        if (complete.get()) {
            return;
        }

        if (!q.isEmpty()) {
            actual.onNext(poll());
        }
        worker.push(this);
    }
}
