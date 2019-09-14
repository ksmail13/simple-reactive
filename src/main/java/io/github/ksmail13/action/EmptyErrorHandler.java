package io.github.ksmail13.action;

import java.util.function.Consumer;

enum EmptyErrorHandler implements Consumer<Throwable> {
    INSTANCE;

    @Override
    public void accept(Throwable throwable) {}
}
