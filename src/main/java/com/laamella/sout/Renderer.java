package com.laamella.sout;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static java.util.stream.Collectors.joining;

abstract class Renderer {
    final Position position;

    Renderer(Position position) {
        this.position = position;
    }

    abstract void render(Object model, Scope scope, Writer outputWriter) throws IOException;
}

class NameRenderer extends Renderer {
    private final String name;
    private final NameResolver nameResolver;
    private final CustomNameRenderer customNameRenderer;
    private final CustomTypeRenderer customTypeRenderer;

    NameRenderer(String name, Position position, NameResolver nameResolver, CustomNameRenderer customNameRenderer, CustomTypeRenderer customTypeRenderer) {
        super(position);
        this.name = name;
        this.customNameRenderer = customNameRenderer;
        this.nameResolver = nameResolver;
        this.customTypeRenderer = customTypeRenderer;
    }

    @Override
    void render(Object model, Scope scope, Writer outputWriter) throws IOException {
        if (customNameRenderer.render(model, name, scope, outputWriter)) {
            return;
        }
        Object subModel = nameResolver.resolveComplexNameOnModel(model, name);
        if (customTypeRenderer.write(subModel, scope, outputWriter)) {
            return;
        }
        if (subModel == null) {
            throw new SoutException("Null value.");
        }
        outputWriter.append(subModel.toString());
    }

    @Override
    public String toString() {
        return "❰" + name + "❱";
    }
}

class ContainerRenderer extends Renderer {
    private final List<Renderer> children;

    ContainerRenderer(Position position, List<Renderer> children) {
        super(position);
        this.children = children;
    }

    @Override
    void render(Object model, Scope scope, Writer outputWriter) throws IOException {
        for (var child : children) {
            child.render(model, scope, outputWriter);
        }
    }

    @Override
    public String toString() {
        return children.stream().map(Object::toString).collect(joining());
    }
}

class LoopRenderer extends Renderer {
    private final String name;
    private final NameResolver nameResolver;
    private final IteratorFactory iteratorFactory;
    private final ContainerRenderer mainPart;
    private final ContainerRenderer separatorPart;
    private final ContainerRenderer leadIn;
    private final ContainerRenderer leadOut;

    LoopRenderer(String name, Position position, NameResolver nameResolver, IteratorFactory iteratorFactory,
                 ContainerRenderer mainPart, ContainerRenderer separatorPart, ContainerRenderer leadIn, ContainerRenderer leadOut) {
        super(position);
        this.name = name;
        this.nameResolver = nameResolver;
        this.iteratorFactory = iteratorFactory;
        this.mainPart = mainPart;
        this.separatorPart = separatorPart;
        this.leadIn = leadIn;
        this.leadOut = leadOut;
    }

    @Override
    public void render(Object model, Scope scope, Writer outputWriter) throws IOException {
        var collection = nameResolver.resolveComplexNameOnModel(model, name);
        var iterator = iteratorFactory.toIterator(collection, scope);
        if (!iterator.hasNext()) {
            // Empty collection, nothing to do.
            return;
        }
        var loopScope = new Scope(scope);

        if (leadIn != null) {
            leadIn.render(model, loopScope, outputWriter);
        }

        var printSeparator = false;
        while (iterator.hasNext()) {
            var listElement = iterator.next();
            if (printSeparator && separatorPart != null) {
                separatorPart.render(listElement, loopScope, outputWriter);
            }
            printSeparator = true;
            mainPart.render(listElement, loopScope, outputWriter);
        }
        if (leadOut != null) {
            leadOut.render(model, loopScope, outputWriter);
        }
    }

    @Override
    public String toString() {
        return '❰' + name +
                (leadIn != null ? "❚" + leadIn : "") +
                "❚" + mainPart +
                (separatorPart != null ? "❚" + separatorPart : "") +
                (leadOut != null ? "❚" + leadOut : "")
                + '❱';
    }
}

class TextRenderer extends Renderer {
    final String text;

    TextRenderer(String text, Position position) {
        super(position);
        this.text = text;
    }

    @Override
    public void render(Object model, Scope scope, Writer outputWriter) throws IOException {
        outputWriter.append(text);
    }

    @Override
    public String toString() {
        return text;
    }
}

