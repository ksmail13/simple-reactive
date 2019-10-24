package io.github.ksmail13.action;

import org.reactivestreams.Subscription;

/**
 * Subscription that can poll the data.
 * @param <T> data type
 */
public interface PollSubscription<T> extends Subscription {

    /**
     * poll data
     * @return data
     */
    T poll();
}
