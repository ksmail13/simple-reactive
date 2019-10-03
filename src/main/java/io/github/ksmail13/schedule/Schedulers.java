package io.github.ksmail13.schedule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Schedulers {

    public static final Scheduler POOLED = new PooledScheduler();
}
