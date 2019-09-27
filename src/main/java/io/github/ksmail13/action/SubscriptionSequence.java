package io.github.ksmail13.action;

import io.github.ksmail13.util.SafeGetter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

@Slf4j
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
        log.trace("requested {} datas", n);
        try {
            List<T> elements = dataGetter.getElements(n);
            for (T element : elements) {
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
