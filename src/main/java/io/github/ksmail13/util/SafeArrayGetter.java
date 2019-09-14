package io.github.ksmail13.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class SafeArrayGetter<T> implements SafeGetter<T> {

    private final T[] data;
    private volatile AtomicLong curr;


    public SafeArrayGetter(T[] data) {
        this.data = data;
        this.curr = new AtomicLong(0);
    }

    @Override
    public List<T> getElements(long n) {
        List<T> result = new ArrayList<T>((int) n);
        long end = curr.get() + n;
        while (curr.get() < end && curr.get() < data.length) {
            result.add(data[(int) curr.get()]);
            curr.incrementAndGet();
        }

        return result;
    }

    @Override
    public boolean isEnd() {
        return curr.get() >= data.length;
    }
}
