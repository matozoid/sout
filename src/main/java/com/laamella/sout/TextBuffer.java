package com.laamella.sout;

/**
 * Simple wrapper around StringBuilder to make the parser's life easier.
 */
final class TextBuffer {
    private final StringBuilder stringBuilder = new StringBuilder();

    boolean isNotEmpty() {
        return stringBuilder.length() > 0;
    }

    void append(int c) {
        stringBuilder.append((char) c);
    }

    String consume(){
        String val = stringBuilder.toString();
        clear();
        return val;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    void clear() {
        stringBuilder.setLength(0);
    }
}
