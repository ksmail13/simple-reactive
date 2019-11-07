package io.github.ksmail13.schedule;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
class WorkerFactory implements ThreadFactory {

    private final String name;
    private AtomicInteger cnt = new AtomicInteger();

    Worker newWorker() {
        return new Worker(name + "-" + cnt.getAndIncrement());
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return newWorker();
    }
}
