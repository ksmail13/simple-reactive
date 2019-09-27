package io.github.ksmail13.action;

import io.github.ksmail13.util.AssertSubscriber;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ManyTest {

    @Test
    public void testSingle() {
        Many.just(1).subscribe(AssertSubscriber.expectResult(1));
    }

    @Test
    public void testArray() {
        Many.just(1, 2, 3, 4, 5).subscribe(AssertSubscriber.expectResult(1, 2, 3, 4, 5));
    }

    @Test
    public void testArrayOverIndex() {
        AssertSubscriber<Object> objectAssertSubscriber = new AssertSubscriber<>();
        objectAssertSubscriber.setCount(10);
        Many.just(1, 2, 3, 4, 5).subscribe(objectAssertSubscriber);
        objectAssertSubscriber.assertion().containsOnly(1, 2, 3, 4, 5);
    }

    @Test
    public void testCollection() {
        AssertSubscriber<Integer> subscriber = new AssertSubscriber<>();
        subscriber.setCount(5);
        Many.fromSequence(Arrays.asList(1, 2, 3, 4, 5)).subscribe(subscriber);
        subscriber.assertion().containsOnly(1, 2, 3, 4, 5);
    }

    @Test
    public void testCollectionOverCount() {
        AssertSubscriber<Integer> subscriber = new AssertSubscriber<>();
        subscriber.setCount(10);
        Many.fromSequence(Arrays.asList(1, 2, 3, 4, 5)).subscribe(subscriber);
        subscriber.assertion().containsOnly(1, 2, 3, 4, 5);
    }

    @Test
    public void testUnbound() {
        AssertSubscriber<Integer> subscriber = new AssertSubscriber<>();
        subscriber.setCount(5);
        Many.fromSequence(IntStream.iterate(1, i -> i + 1).iterator()).subscribe(subscriber);
        subscriber.assertion().startsWith(1, 2, 3, 4, 5);
    }


    @Test
    public void testTake() {
        AssertSubscriber<Integer> subscriber = new AssertSubscriber<>();
        subscriber.setCount(5);
        Many.fromSequence(IntStream.iterate(1, i -> i + 1).iterator())
                .take(3)
                .subscribe(subscriber);
        subscriber.assertion().containsExactly(1, 2, 3);
    }

}