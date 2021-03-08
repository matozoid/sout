package com.laamella.sout;

import java.io.IOException;
import java.io.Writer;

@FunctionalInterface
public interface CustomTypeRenderer {
    /**
     * Custom rendering for types.
     *
     * @param name
     * @param parts
     * @param model        the value to write to the output.
     * @param scope
     * @param parentModel
     * @param parentScope
     * @param outputWriter the output.
     * @return true if this {@link CustomTypeRenderer} has written the type and no further handling is wanted.
     */
    boolean render(String name, Renderable[] parts, Object model, Scope scope, Object parentModel, Scope parentScope, Position position, Writer outputWriter) throws IOException;
}