package com.laamella.sout;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

public class DataConverterTest {
    private final SoutConfiguration defaultConfiguration = new SoutConfiguration('{', '|', '}', '\\', emptyList(), emptyList());
    private final DataConverter dataConverter = new DataConverter(defaultConfiguration);

    @Test
    void listsGetConvertedToLists() {
        var objects = (List<Integer>) newArrayList(dataConverter.toIterator(asList(1, 2, 3)));
        assertThat(objects).containsExactly(1, 2, 3);
    }

    @Test
    void arraysGetConvertedToLists() {
        var objects = (List<Integer>) newArrayList(dataConverter.toIterator(new int[]{1, 2, 3}));
        assertThat(objects).containsExactly(1, 2, 3);
    }

    @Test
    void streamsGetConvertedToLists() {
        var objects = (List<Integer>) newArrayList(dataConverter.toIterator(Stream.of(1, 2, 3)));
        assertThat(objects).containsExactly(1, 2, 3);
    }

    @Test
    void renderStringToText() throws IOException {
        var output = new StringWriter();
        dataConverter.renderAsText("abc", output);
        assertThat(output.toString()).isEqualTo("abc");
    }

    @Test
    void renderIntToText() throws IOException {
        var output = new StringWriter();
        dataConverter.renderAsText(123, output);
        assertThat(output.toString()).isEqualTo("123");
    }

    @Test
    void renderNullToText() throws IOException {
        var output = new StringWriter();
        dataConverter.renderAsText(null, output);
        assertThat(output.toString()).isEqualTo("");
    }

    @Test
    void specialRenderer() throws IOException {
        TypeHandler specialTypeHandler = (value, output) -> {
            if (value instanceof Integer) {
                output.append("INT");
                return true;
            }
            return false;
        };
        var configuration = new SoutConfiguration('{', '|', '}', '\\', emptyList(), singletonList(specialTypeHandler));
        var output = new StringWriter();
        new DataConverter(configuration).renderAsText(123, output);
        assertThat(output.toString()).isEqualTo("INT");
    }
}