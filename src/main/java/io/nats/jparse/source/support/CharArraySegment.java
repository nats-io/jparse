/*
 * Copyright 2013-2023 Richard M. Hightower
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.nats.jparse.source.support;

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
            for (int i = offset, j = other.offset; i < end; i++, j++) {
                final char cOther = other.data[j];
                final char cThis = data[i];
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
            for (int i = offset, j = 0; i < end; i++, j++) {
                final char cOther = other.charAt(j);
                final char cThis = data[i];
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
