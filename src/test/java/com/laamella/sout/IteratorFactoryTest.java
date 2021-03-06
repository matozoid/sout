package com.laamella.sout;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.newArrayList;

@SuppressWarnings("unchecked")
public class IteratorFactoryTest {
    private final IteratorFactory iteratorFactory = new IteratorFactory((model, scope, position) -> null);
    private final Scope scope = new Scope(null);
    private final Position pos = new Position(3, 4);

    @Test
    public void listsGetConvertedToLists() {
        var objects = (List<Integer>) newArrayList(iteratorFactory.toIterator(asList(1, 2, 3), scope, pos));
        assertThat(objects).containsExactly(1, 2, 3);
    }

    @Test
    public void byteArraysGetConvertedToLists() {
        var objects = (List<Byte>) newArrayList(iteratorFactory.toIterator(new byte[]{1, 2, 3}, scope, pos));
        assertThat(objects).containsExactly((byte) 1, (byte) 2, (byte) 3);
    }

    @Test
    public void shortArraysGetConvertedToLists() {
        var objects = (List<Short>) newArrayList(iteratorFactory.toIterator(new short[]{1, 2, 3}, scope, pos));
        assertThat(objects).containsExactly((short) 1, (short) 2, (short) 3);
    }

    @Test
    public void intArraysGetConvertedToLists() {
        var objects = (List<Integer>) newArrayList(iteratorFactory.toIterator(new int[]{1, 2, 3}, scope, pos));
        assertThat(objects).containsExactly(1, 2, 3);
    }

    @Test
    public void longArraysGetConvertedToLists() {
        var objects = (List<Long>) newArrayList(iteratorFactory.toIterator(new long[]{1, 2, 3}, scope, pos));
        assertThat(objects).containsExactly(1L, 2L, 3L);
    }

    @Test
    public void floatArraysGetConvertedToLists() {
        var objects = (List<Float>) newArrayList(iteratorFactory.toIterator(new float[]{1.1f, 2.2f, 3.3f}, scope, pos));
        assertThat(objects).containsExactly(1.1f, 2.2f, 3.3f);
    }

    @Test
    public void doubleArraysGetConvertedToLists() {
        var objects = (List<Double>) newArrayList(iteratorFactory.toIterator(new double[]{1.1, 2.2, 3.3}, scope, pos));
        assertThat(objects).containsExactly(1.1, 2.2, 3.3);
    }

    @Test
    public void booleanArraysGetConvertedToLists() {
        var objects = (List<Boolean>) newArrayList(iteratorFactory.toIterator(new boolean[]{true, false, true}, scope, pos));
        assertThat(objects).containsExactly(true, false, true);
    }

    @Test
    public void charArraysGetConvertedToLists() {
        var objects = (List<Character>) newArrayList(iteratorFactory.toIterator(new char[]{'1', '2', '3'}, scope, pos));
        assertThat(objects).containsExactly('1', '2', '3');
    }

    @Test
    public void streamsGetConvertedToLists() {
        var objects = (List<Integer>) newArrayList(iteratorFactory.toIterator(Stream.of(1, 2, 3), scope, pos));
        assertThat(objects).containsExactly(1, 2, 3);
    }

    @Test
    public void nullsAreNotIterable() {
        assertThatThrownBy(() -> iteratorFactory.toIterator(null, scope, pos))
                .isInstanceOf(SoutException.class)
                .hasMessage("4:3 Trying to nest into null.");
    }

    @Test
    public void customIteratorFactoryMakesNullsIterable() {
        IteratorFactory iteratorFactory = new IteratorFactory((model, scope, position) -> model == null ? emptyList().iterator() : null);
        var objects = (List<Integer>) newArrayList(iteratorFactory.toIterator(null, scope, pos));
        assertThat(objects).containsExactly();
    }

    @Test
    public void customIteratorForAssertjTuple() {
        var tuple = tuple("ABC", 123, new Date());
        IteratorFactory iteratorFactory = new IteratorFactory((model, scope, position) -> {
            if (model instanceof Tuple) {
                return ((Tuple) model).toList().iterator();
            }
            return null;
        });
        var objects = (List<Object>) newArrayList(iteratorFactory.toIterator(tuple, scope, pos));
        Object[] tupleArray = tuple.toArray();
        assertThat(objects).containsExactly(tupleArray[0], tupleArray[1], tupleArray[2]);
    }
}