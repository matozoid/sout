package com.laamella.sout;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

/**
 * This iterator factory is always in use, creating iterators for the collections, arrays, etc. in the JDK.
 */
class IteratorFactory implements CustomIteratorFactory {
    private final CustomIteratorFactory customIteratorFactory;

    IteratorFactory(CustomIteratorFactory customIteratorFactory) {
        this.customIteratorFactory = customIteratorFactory;
    }

    @Override
    public Iterator<?> toIterator(Object model, Scope scope, Position position) {
        Iterator<?> iterator = customIteratorFactory.toIterator(model, scope, position);
        if (iterator != null) {
            return iterator;
        } else if (model == null) {
            throw new SoutException(position, "Trying to nest into null.");
        } else if (model instanceof List) {
            return ((List<?>) model).iterator();
        } else if (model instanceof Object[]) {
            return stream((Object[]) model).iterator();
        } else if (model instanceof boolean[]) {
            return new BooleanArrayIterator((boolean[]) model);
        } else if (model instanceof byte[]) {
            return new ByteArrayIterator((byte[]) model);
        } else if (model instanceof char[]) {
            return new CharacterArrayIterator((char[]) model);
        } else if (model instanceof short[]) {
            return new ShortArrayIterator((short[]) model);
        } else if (model instanceof int[]) {
            return stream((int[]) model).boxed().iterator();
        } else if (model instanceof long[]) {
            return new LongArrayIterator((long[]) model);
        } else if (model instanceof float[]) {
            return new FloatArrayIterator((float[]) model);
        } else if (model instanceof double[]) {
            return new DoubleArrayIterator((double[]) model);
        } else if (model instanceof Stream) {
            return ((Stream<?>) model).iterator();
        } else if (model instanceof Iterator) {
            return (Iterator<?>) model;
        } else if (model instanceof Iterable) {
            return ((Iterable<?>) model).iterator();
        }
        return null;
    }

    static class ByteArrayIterator implements Iterator<Byte> {
        final byte[] a;
        int i = 0;

        ByteArrayIterator(byte[] a) {
            this.a = a;
        }

        public boolean hasNext() {
            return i < a.length;
        }

        public Byte next() {
            return a[i++];
        }
    }

    static class ShortArrayIterator implements Iterator<Short> {
        final short[] a;
        int i = 0;

        ShortArrayIterator(short[] a) {
            this.a = a;
        }

        public boolean hasNext() {
            return i < a.length;
        }

        public Short next() {
            return a[i++];
        }
    }

    static class FloatArrayIterator implements Iterator<Float> {
        final float[] a;
        int i = 0;

        FloatArrayIterator(float[] a) {
            this.a = a;
        }

        public boolean hasNext() {
            return i < a.length;
        }

        public Float next() {
            return a[i++];
        }
    }

    static class DoubleArrayIterator implements Iterator<Double> {
        final double[] a;
        int i = 0;

        DoubleArrayIterator(double[] a) {
            this.a = a;
        }

        public boolean hasNext() {
            return i < a.length;
        }

        public Double next() {
            return a[i++];
        }
    }

    static class CharacterArrayIterator implements Iterator<Character> {
        final char[] a;
        int i = 0;

        CharacterArrayIterator(char[] a) {
            this.a = a;
        }

        public boolean hasNext() {
            return i < a.length;
        }

        public Character next() {
            return a[i++];
        }
    }

    static class BooleanArrayIterator implements Iterator<Boolean> {
        final boolean[] a;
        int i = 0;

        BooleanArrayIterator(boolean[] a) {
            this.a = a;
        }

        public boolean hasNext() {
            return i < a.length;
        }

        public Boolean next() {
            return a[i++];
        }
    }

    static class LongArrayIterator implements Iterator<Long> {
        final long[] a;
        int i = 0;

        LongArrayIterator(long[] a) {
            this.a = a;
        }

        public boolean hasNext() {
            return i < a.length;
        }

        public Long next() {
            return a[i++];
        }
    }

}
