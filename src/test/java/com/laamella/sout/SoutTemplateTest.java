package com.laamella.sout;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SoutTemplateTest {
    @Test
    void emptyMainPart() throws IOException, IllegalAccessException {
        var template = parse("Hello {name}, {friends|{} and }uh!");
        var data = ImmutableMap.of(
                "name", "Piet",
                "friends", ImmutableList.of("Hans", "Henk"));

        assertRendered("Hello Piet, Hans and Henk and uh!", template, data);
    }

    @Test
    void oneElementLoop() throws IOException, IllegalAccessException {
        var template = parse("Hello {name}, {friends|{name} and }uh!");
        var data = ImmutableMap.of(
                "name", "Piet",
                "friends", ImmutableList.of(
                        ImmutableMap.of("name", "Hans"),
                        ImmutableMap.of("name", "Henk")));

        assertRendered("Hello Piet, Hans and Henk and uh!", template, data);
    }

    @Test
    void twoElementLoop() throws IOException, IllegalAccessException {
        var template = parse("Hello {name}, {friends|{name}| and }!");
        var data = ImmutableMap.of(
                "name", "Piet",
                "friends", ImmutableList.of(
                        ImmutableMap.of("name", "Hans"),
                        ImmutableMap.of("name", "Henk")));

        assertRendered("Hello Piet, Hans and Henk!", template, data);
    }

    @Test
    void fourElementLoop() throws IOException, IllegalAccessException {
        var template = parse("Hello {name}{friends| and your {friendState} friends |{name}| and |! {exclamation}}");
        var data = ImmutableMap.of(
                "name", "Piet",
                "friendState", "happy",
                "exclamation", "hurray!",
                "friends", ImmutableList.of(
                        ImmutableMap.of("name", "Hans"),
                        ImmutableMap.of("name", "Henk")));

        assertRendered("Hello Piet and your happy friends Hans and Henk! hurray!", template, data);
    }

    @Test
    void nestedName() throws IOException, IllegalAccessException {
        var template = parse("{recurser|{value}} {recurser|{recurser|{value}}} {recurser|{recurser|{recurser|{value}}}}");
        var data = new TestModel();

        assertRendered("1 2 3", template, data);
    }

    @Test
    void nestedNameWithDots() throws IOException, IllegalAccessException {
        var template = parse("{recurser.value} {recurser.recurser.value} {recurser.recurser.recurser.value}");
        var data = new TestModel();

        assertRendered("1 2 3", template, data);
    }


    private SoutTemplate parse(String template) throws IOException {
        var configuration = new SoutConfiguration('{', '|', '}', '\\', emptyList(), emptyList());
        return SoutTemplate.parse(new StringReader(template), configuration);
    }

    private void assertRendered(String expected, SoutTemplate template, Object data) throws IOException, IllegalAccessException {
        var output = new StringWriter();
        template.render(data, output);
        assertEquals(expected, output.toString());
    }

}
