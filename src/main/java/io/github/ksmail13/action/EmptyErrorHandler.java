package io.github.ksmail13.action;

import io.github.ksmail13.exception.UncaughtErrorException;

import java.util.function.Consumer;

/**
 * default error handler
 * throw exception when error occurred
 */
enum EmptyErrorHandler implements Consumer<Throwable> {
    INSTANCE;

    @Override
    public void accept(Throwable throwable) {
        throw new UncaughtErrorException(throwable);
    }
}
