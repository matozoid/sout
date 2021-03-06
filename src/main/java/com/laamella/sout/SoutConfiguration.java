package com.laamella.sout;

/**
 * Everything that is configurable.
 */
public class SoutConfiguration {
    final char openChar;
    final char escapeChar;
    final char closeChar;
    final char separatorChar;
    final CustomNameRenderer customNameRenderer;
    final CustomTypeRenderer customTypeRenderer;
    final CustomIteratorFactory customIteratorFactory;

    /**
     * @param openChar              the character that opens a name or a nesting, like "{" or "<"
     * @param separatorChar         the character that separates parts of a nesting, like "|"
     * @param closeChar             the character that closes a name or nesting, like "}" or ">"
     * @param escapeChar            the character that can escape the openChar, separatorChar, and closeChar
     * @param customNameRenderer    a {@link CustomNameRenderer} that will be asked if they can render a name. Can be null, meaning no special name rendering.
     * @param customTypeRenderer    a  {@link CustomTypeRenderer} that will be asked if they can render a specific type (class) that was encountered in the model. Can be null, meaning no special type rendering.
     * @param customIteratorFactory a {@link CustomIteratorFactory} that will be asked to create an iterator for a specific type. Can be null, meaning no special ways to create iterators are required.
     */
    public SoutConfiguration(
            char openChar, char separatorChar, char closeChar, char escapeChar,
            CustomNameRenderer customNameRenderer, CustomTypeRenderer customTypeRenderer, CustomIteratorFactory customIteratorFactory) {
        this.openChar = openChar;
        this.escapeChar = escapeChar;
        this.closeChar = closeChar;
        this.separatorChar = separatorChar;
        this.customNameRenderer = customNameRenderer == null ? (name, parts, model, scope, position, outputWriter) -> false : customNameRenderer;
        this.customTypeRenderer = customTypeRenderer == null ? (name, parts, model, scope, parentModel, parentScope, position, outputWriter) -> false : customTypeRenderer;
        this.customIteratorFactory = customIteratorFactory == null ? (model, scope, position) -> null : customIteratorFactory;
    }
}
