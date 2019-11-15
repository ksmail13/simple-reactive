package io.github.ksmail13.util;

import lombok.extern.java.Log;
import org.reactivestreams.Subscription;

import java.util.function.Supplier;
import java.util.logging.Level;

@Log
public class LogListenSubscriber<T> implements ListenSubscriber<T> {
    static {
        log.setLevel(Level.FINEST);
        log.config("Set log level finest");
    }


    @Override
    public void onSubscribe(Subscription s) {
        log.fine(logMessage("onSubscribe(%s)", s));
    }

    @Override
    public void onNext(T t) {
        log.info(logMessage("onNext(%s)", t));
    }

    @Override
    public void onError(Throwable t) {
        log.fine(logMessage("onError(%s)", t));
    }

    @Override
    public void onComplete() {
        log.fine(logMessage("onComplete"));
    }

    private Supplier<String> logMessage(String format, Object... args) {
        return () -> "[" + Thread.currentThread().getName() + "]" + " - " + String.format(format, args) + "\n";
    }
}
