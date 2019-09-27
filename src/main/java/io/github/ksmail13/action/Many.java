package io.github.ksmail13.action;

import io.github.ksmail13.util.SafeArrayGetter;
import io.github.ksmail13.util.SafeIteratorGetter;
import io.github.ksmail13.util.SafeSequenceGetter;
import org.reactivestreams.Publisher;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Base Publisher type
 * @param <T> data type
 */
public interface Many<T> extends Publisher<T> {

    /**
     * produce only single data
     * @param data data
     * @param <V> data type
     * @return single data producer
     */
    static <V> Many<V> just(V data) {
        return subscriber -> subscriber.onSubscribe(new SubscriptionOne<>(data, subscriber));
    }

    /**
     * produce data from array
     * @param data data
     * @param <V> data type
     * @return array data producer
     */
    static <V> Many<V> just(V... data) {
        return s -> s.onSubscribe(new SubscriptionSequence<>(new SafeArrayGetter<>(data), s));
    }

    /**
     * produce data from collection
     * @param data data
     * @param <V> data type
     * @return collection data producer
     */
    static <V> Many<V> fromSequence(Collection<V> data) {
        return s -> s.onSubscribe(new SubscriptionSequence<>(new SafeSequenceGetter<>(data), s));
    }

    /**
     * produce data from iterator
     * @param data data iterator
     * @param <V> data type
     * @return iterator data producer
     */
    static <V> Many<V> fromSequence(Iterator<V> data) {
        return s -> s.onSubscribe(new SubscriptionSequence<>(new SafeIteratorGetter<>(data), s));
    }

    /**
     * take some n of datas
     * @param n n is
     * @return
     */
    default Many<T> take(int n) {
        return s -> s.onSubscribe(new TakeManySubscription<T>(this, n, s));
    }

    default void subscribe(Consumer<T> onNext) {
        this.subscribe(onNext, EmptyErrorHandler.INSTANCE, EmptyCompleteHandler.INSTANCE);
    }

    default void subscribe(Consumer<T> onNext, Consumer<Throwable> onError) {
        this.subscribe(onNext, onError, EmptyCompleteHandler.INSTANCE);
    }

    default void subscribe(Consumer<T> onNext, Consumer<Throwable> onError, Runnable onComplete) {
        this.subscribe(new SimpleSubscriber<>(onNext, onError, onComplete));
    }

}
