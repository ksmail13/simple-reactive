package io.github.ksmail13.util;

import lombok.Setter;
import lombok.extern.java.Log;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.ListAssert;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Log
public class AssertSubscriber<T> implements Subscriber<T> {
    private List<T> results = new ArrayList<>();
    @Setter
    private ListenSubscriber<T> eventListener = new LogListenSubscriber<>();
    private Throwable error;

    private Throwable expectError;
    private T[] expectResult;

    private boolean complete;

    private int count = Integer.MAX_VALUE;

    public static <T> AssertSubscriber<T> expectResult(T... expects) {
        AssertSubscriber<T> tAssertSubscriber = new AssertSubscriber<>(expects, null);
        tAssertSubscriber.setCount(expects.length);
        return tAssertSubscriber;
    }

    public static <T> AssertSubscriber<T> expectError(Throwable error) {
        return new AssertSubscriber<>(null, error);
    }

    public AssertSubscriber() {
        this(null, null);
    }

    private AssertSubscriber(T[] expectResult, Throwable expectError) {
        this.expectError = expectError;
        this.expectResult = expectResult;
    }

    @Override
    public void onSubscribe(Subscription s) {
        if(eventListener != null) {
            eventListener.onSubscribe(s);
        }
        s.request(count);
    }

    @Override
    public void onNext(T t) {
        if (eventListener != null) {
            eventListener.onNext(t);
        }
        results.add(t);
    }

    @Override
    public void onError(Throwable t) {
        if (eventListener != null) {
            eventListener.onError(t);
        }

        error = t;
    }

    @Override
    public void onComplete() {
        if (eventListener != null) {
            eventListener.onComplete();
        }

        if (complete) {
            fail("duplicate end");
        }

        complete = true;

        if (expectResult != null) {
            assertThat(results).contains(expectResult);
        }

        if (expectError != null) {
            assertThat(error).isEqualTo(expectError);
        }
    }

    public ListAssert<T> assertion() {
        return assertThat(results);
    }

    public AbstractThrowableAssert<?, ? extends Throwable> errorAssertion() {
        return assertThat(error);
    }

    public void setCount(int count) {
        this.count = count;
    }
}
