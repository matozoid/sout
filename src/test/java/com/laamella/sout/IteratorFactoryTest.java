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
    private final IteratorFactory iteratorFactory = new IteratorFactory(model -> null);

    @Test
    public void listsGetConvertedToLists() {
        var objects = (List<Integer>) newArrayList(iteratorFactory.toIterator(asList(1, 2, 3)));
        assertThat(objects).containsExactly(1, 2, 3);
    }

    @Test
    public void arraysGetConvertedToLists() {
        var objects = (List<Integer>) newArrayList(iteratorFactory.toIterator(new int[]{1, 2, 3}));
        assertThat(objects).containsExactly(1, 2, 3);
    }

    @Test
    public void streamsGetConvertedToLists() {
        var objects = (List<Integer>) newArrayList(iteratorFactory.toIterator(Stream.of(1, 2, 3)));
        assertThat(objects).containsExactly(1, 2, 3);
    }

    @Test
    public void nullsAreNotIterable() {
        assertThatThrownBy(() -> iteratorFactory.toIterator(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trying to loop over null.");
    }

    @Test
    public void customIteratorFactoryMakesNullsIterable() {
        IteratorFactory iteratorFactory = new IteratorFactory(model -> model == null ? emptyList().iterator() : null);
        var objects = (List<Integer>) newArrayList(iteratorFactory.toIterator(null));
        assertThat(objects).containsExactly();
    }

    @Test
    public void customIteratorForAssertjTuple() {
        var tuple = tuple("ABC", 123, new Date());
        IteratorFactory iteratorFactory = new IteratorFactory(model -> {
            if (model instanceof Tuple) {
                return ((Tuple) model).toList().iterator();
            }
            return null;
        });
        var objects = (List<Object>) newArrayList(iteratorFactory.toIterator(tuple));
        Object[] tupleArray = tuple.toArray();
        assertThat(objects).containsExactly(tupleArray[0], tupleArray[1], tupleArray[2]);
    }
}