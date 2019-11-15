package io.github.ksmail13.action;

import io.github.ksmail13.schedule.Scheduler;
import io.github.ksmail13.util.SafeIteratorGetter;
import io.github.ksmail13.util.SafeSequenceGetter;
import org.reactivestreams.Publisher;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Base Publisher type
 *
 * @param <T> data type
 */
public abstract class Many<T> implements Publisher<T> {

    /**
     * produce only single data
     *
     * @param data data
     * @param <V>  data type
     * @return single data producer
     */
    public static <V> Many<V> just(V data) {
        return new ManyJust<>(data);
    }

    /**
     * produce data from array
     *
     * @param data data
     * @param <V>  data type
     * @return array data producer
     */
    public static <V> Many<V> just(V... data) {
        return new ManyArray<>(data);
    }

    /**
     * produce data from collection
     *
     * @param data data
     * @param <V>  data type
     * @return collection data producer
     */
    public static <V> Many<V> fromSequence(Collection<V> data) {
        return new ManySequence<>(new SafeSequenceGetter<>(data));
    }

    /**
     * produce data from iterator
     *
     * @param data data iterator
     * @param <V>  data type
     * @return iterator data producer
     */
    public static <V> Many<V> fromSequence(Iterator<V> data) {
        return new ManySequence<>(new SafeIteratorGetter<>(data));
    }

    public Many<T> take(long cnt) {
        return new ManyTake<>(this, cnt);
    }

    public <R> Many<R> map(Function<T, R> transform) {
        return new ManyMap<>(this, transform);
    }

    public Many<T> subscribeOn(Scheduler executorService) {
        return new ManySubscribeOn<>(this, executorService);
    }

    public void subscribe(Consumer<T> onNext) {
        this.subscribe(onNext, EmptyErrorHandler.INSTANCE, EmptyCompleteHandler.INSTANCE);
    }

    public void subscribe(Consumer<T> onNext, Consumer<Throwable> onError) {
        this.subscribe(onNext, onError, EmptyCompleteHandler.INSTANCE);
    }

    public void subscribe(Consumer<T> onNext, Consumer<Throwable> onError, Runnable onComplete) {
        this.subscribe(new SimpleSubscriber<>(onNext, onError, onComplete));
    }

}
