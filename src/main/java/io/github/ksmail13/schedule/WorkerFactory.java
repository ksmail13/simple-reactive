package io.github.ksmail13.schedule;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadFactory;

@RequiredArgsConstructor
class WorkerFactory {

    private final String name;
    private int cnt = 0;

    Worker newWorker() {
        return new Worker(name + "-" + cnt++);
    }
}
