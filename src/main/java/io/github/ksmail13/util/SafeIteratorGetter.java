package io.github.ksmail13.util;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class SafeIteratorGetter<T> implements SafeGetter<T> {

    private final Iterator<T> iterator;

    @Override
    public List<T> getElements(long n) {
        List<T> list = new ArrayList<>();

        for (long i = 0; i < n; i++) {
            if (isEnd()) {
                break;
            }

            list.add(iterator.next());
        }

        return list;
    }

    @Override
    public boolean isEnd() {
        return !iterator.hasNext();
    }
}
