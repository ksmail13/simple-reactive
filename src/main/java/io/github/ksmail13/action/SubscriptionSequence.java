package io.github.ksmail13.action;

import io.github.ksmail13.util.SafeGetter;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@RequiredArgsConstructor
class SubscriptionSequence<T> implements Subscription {

    private final SafeGetter<T> dataGetter;
    private final Subscriber<? super T> subscriber;
    private volatile boolean stopped;

    @Override
    public void request(long n) {
        if (stopped) {
            return;
        }

        try {
            for (T element : dataGetter.getElements(n)) {
                if (stopped) {
                    break;
                }
                subscriber.onNext(element);
            }
            if (dataGetter.isEnd() || stopped) {
                subscriber.onComplete();
            }
        } catch (Exception e) {
            subscriber.onError(e);
        }
    }

    @Override
    public void cancel() {
        stopped = true;
        subscriber.onComplete();
    }
}
