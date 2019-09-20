package io.github.ksmail13.util;

import java.util.List;

/**
 * get no of data
 *
 * @param <T> data type
 */
public interface SafeGetter<T> {

    List<T> getElements(long n);

    boolean isEnd();

}
