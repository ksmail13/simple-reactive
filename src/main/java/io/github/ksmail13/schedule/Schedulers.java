package io.github.ksmail13.schedule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Schedulers {

    private static final Scheduler POOLED = new PooledScheduler();

    public static Scheduler pooled() {
        return POOLED;
    }

    public static Scheduler newPooled() {
        return new PooledScheduler();
    }

    public static Scheduler newPooled(int parallelism) {
        return new PooledScheduler(parallelism);
    }
}
