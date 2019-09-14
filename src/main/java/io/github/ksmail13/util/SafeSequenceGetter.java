package io.github.ksmail13.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SafeSequenceGetter<T> implements SafeGetter<T> {

    private final List<T> dataList;
    private final AtomicInteger curr;

    public SafeSequenceGetter(Collection<T> data) {
        this.dataList = new ArrayList<>(data);
        this.curr = new AtomicInteger();
    }

    @Override
    public List<T> getElements(long size) {
        if (isEnd()) {
            return Collections.emptyList();
        }
        int start = curr.get();
        int end = Math.min(curr.addAndGet((int)size), dataList.size());
        return dataList.subList(start, end);
    }

    @Override
    public boolean isEnd() {
        return curr.get() >= dataList.size();
    }

}
