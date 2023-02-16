package com.cloudurable.jparse.source.support;

public class CharArraySegment implements CharSequence {

    private final int offset;
    private final int length;
    private final char[] data;

    public CharArraySegment(int offset, int length, char[] data) {
        this.offset = offset;
        this.length = length;
        this.data = data;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        return data[offset + index];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new CharArraySegment(start + offset, end - start, data);
    }

    @Override
    public String toString() {
        return new String(data, offset, length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof CharArraySegment) {
            CharArraySegment other = (CharArraySegment) o;

            if (other.length != this.length) {
                return false;
            }

            final int end = length + offset;
            for (int i = offset,  j = other.offset; i < end; i++, j++) {
                final var cOther = other.data[j];
                final var cThis = data[i];
                if (cOther != cThis) {
                    return false;
                }
            }
            return true;
        } else if (o instanceof CharSequence) {
            CharSequence other = (CharSequence) o;

            if (other.length() != this.length) {
                return false;
            }
            final int end = length + offset;
            for (int i = offset,  j = 0; i < end; i++, j++) {
                final var cOther = other.charAt(j);
                final var cThis = data[i];
                if (cOther != cThis) {
                    return false;
                }
            }
            return true;
        }

        return false;

    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
