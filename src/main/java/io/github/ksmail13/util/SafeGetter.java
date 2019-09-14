package io.github.ksmail13.util;

import java.util.List;

public interface SafeGetter<T> {

    List<T> getElements(long n);

    boolean isEnd();

}
