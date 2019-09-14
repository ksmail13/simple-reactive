package io.github.ksmail13.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class SafeGetterTest {

    @Test
    public void testArray() {
        Integer[] arr = {1, 2, 3, 4, 5, 6};

        SafeGetter<Integer> tSafeArrayGetter = new SafeArrayGetter<>(arr);
        assertThat(tSafeArrayGetter.getElements(3)).containsExactly(1, 2, 3);
        assertThat(tSafeArrayGetter.getElements(3)).containsExactly(4, 5, 6);
    }

    @Test
    public void testCollection() {
        List<Integer> arr = Arrays.asList(1, 2, 3, 4, 5, 6);

        SafeGetter<Integer> tSafeArrayGetter = new SafeSequenceGetter<>(arr);
        assertThat(tSafeArrayGetter.getElements(3)).containsExactly(1, 2, 3);
        assertThat(tSafeArrayGetter.getElements(3)).containsExactly(4, 5, 6);
    }

    @Test
    public void testUnboundIterator() {
        Iterator<Integer> arr = IntStream.iterate(1, i -> i + 1).boxed().iterator();

        SafeGetter<Integer> tSafeArrayGetter = new SafeIteratorGetter<>(arr);
        assertThat(tSafeArrayGetter.getElements(3)).containsExactly(1, 2, 3);
        assertThat(tSafeArrayGetter.getElements(3)).containsExactly(4, 5, 6);
    }
}