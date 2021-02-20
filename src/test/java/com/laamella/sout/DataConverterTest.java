package com.laamella.sout;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

@SuppressWarnings("unchecked")
public class DataConverterTest {
    private final DataConverter dataConverter = new DataConverter();

    @Test
    public void listsGetConvertedToLists() {
        var objects = (List<Integer>) newArrayList(dataConverter.toIterator(asList(1, 2, 3)));
        assertThat(objects).containsExactly(1, 2, 3);
    }

    @Test
    public void arraysGetConvertedToLists() {
        var objects = (List<Integer>) newArrayList(dataConverter.toIterator(new int[]{1, 2, 3}));
        assertThat(objects).containsExactly(1, 2, 3);
    }

    @Test
    public void streamsGetConvertedToLists() {
        var objects = (List<Integer>) newArrayList(dataConverter.toIterator(Stream.of(1, 2, 3)));
        assertThat(objects).containsExactly(1, 2, 3);
    }
}