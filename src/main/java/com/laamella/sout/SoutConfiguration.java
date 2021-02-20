package com.laamella.sout;

/**
 * Everything that is configurable.
 */
public class SoutConfiguration {
    final char openChar;
    final char escapeChar;
    final char closeChar;
    final char separatorChar;
    final NameRenderer nameRenderer;
    final TypeRenderer typeRenderer;

    /**
     * @param openChar      the character that opens a name or loop, like "{" or "<"
     * @param separatorChar the character that separates parts of a loop, like "|"
     * @param closeChar     the character that closes a name or loop, like "}" or ">"
     * @param escapeChar    the character that can escape the openChar, separatorChar, and closeChar
     * @param nameRenderer  a {@link NameRenderer} that will be asked if they can render a simple name (so no loops.) Can be null, meaning no special name rendering.
     * @param typeRenderer  a list of {@link TypeRenderer} that will be asked if they can render a specific type (class) that was encountered in the model. Can be null, meaning no special type rendering.
     */
    public SoutConfiguration(
            char openChar, char separatorChar, char closeChar, char escapeChar,
            NameRenderer nameRenderer, TypeRenderer typeRenderer) {
        this.openChar = openChar;
        this.escapeChar = escapeChar;
        this.closeChar = closeChar;
        this.separatorChar = separatorChar;
        this.nameRenderer = nameRenderer == null ? (model, name, outputWriter) -> false : nameRenderer;
        this.typeRenderer = typeRenderer == null ? (model, outputWriter) -> false : typeRenderer;
    }
}
