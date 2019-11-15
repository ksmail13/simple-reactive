package io.github.ksmail13.action;

import org.reactivestreams.Subscription;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Queue;

/**
 * Subscription that can poll the data.
 * @param <T> data type
 */
public abstract class QueueSubscription<T> extends AbstractQueue<T> implements Subscription, Queue<T> {
    protected Queue<T> delegation;

    @Override
    public Iterator<T> iterator() {
        return delegation.iterator();
    }

    @Override
    public int size() {
        return delegation.size();
    }

    @Override
    public boolean offer(T t) {
        return delegation.offer(t);
    }

    @Override
    public T poll() {
        return delegation.poll();
    }

    @Override
    public T peek() {
        return delegation.peek();
    }
}
