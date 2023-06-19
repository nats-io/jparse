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
package io.nats.jparse.node;

import io.nats.jparse.node.support.CharSequenceUtils;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.token.Token;

import java.util.Collections;
import java.util.List;

/**
 * This class represents a StringNode in the JParse library.
 * It is a specialized ScalarNode that stores a string value.
 *
 * <p>The StringNode contains information about the token, source, start and end indices,
 * and whether the string should be encoded by default.
 *
 * <p>It provides methods to access the string value and perform operations on the string,
 * such as getting the length, retrieving characters, and creating substrings.
 *
 * <p>The StringNode implements the CharSequence interface and overrides the equals and hashCode methods
 * for proper comparison and hashing of the string value.
 *
 * @see io.nats.jparse.node.ScalarNode
 * @see java.lang.CharSequence
 */
public class StringNode implements ScalarNode, CharSequence {

    private final Token token;
    private final CharSource source;
    private final int length;
    private final int start;
    private final int end;
    private final boolean encodeStringByDefault;
    private int hashCode = 0;
    private boolean hashCodeSet = false;

    @Override
    public NodeType type() {
        return NodeType.STRING;
    }

    @Override
    public List<Token> tokens() {
        return Collections.singletonList(this.token);
    }

    @Override
    public Token rootElementToken() {
        return token;
    }

    @Override
    public CharSource charSource() {
        return source;
    }

    @Override
    public Object value() {
        return toString();
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        return source.getChartAt(token.startIndex + index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return source.getCharSequence(start + this.start, end + this.start);
    }

    /**
     * Constructs a StringNode with the specified token and source.
     *
     * @param token                 the token representing the string
     * @param source                the source containing the string
     * @param encodeStringByDefault flag indicating whether the string should be encoded by default
     */
    public StringNode(Token token, CharSource source, boolean encodeStringByDefault) {
        this.token = token;
        this.source = source;
        start = token.startIndex;
        end = token.endIndex;
        this.encodeStringByDefault = encodeStringByDefault;
        this.length = token.endIndex - token.startIndex;
    }

    /**
     * Constructs a StringNode with the specified token and source.
     * The string is encoded by default.
     *
     * @param token  the token representing the string
     * @param source the source containing the string
     */
    public StringNode(Token token, CharSource source) {
        this.token = token;
        this.source = source;
        start = token.startIndex;
        end = token.endIndex;
        this.encodeStringByDefault = true;
        this.length = token.endIndex - token.startIndex;
    }


    /**
     * Returns a CharSequence representing the content of the StringNode.
     *
     * @return a CharSequence representing the content of the StringNode
     */
    public CharSequence charSequence() {
        return source.getCharSequence(this.start, this.end);
    }

    /**
     * Returns a String representation of the StringNode.
     * If encodeStringByDefault is true, the string will be encoded using the source's encoding if needed.
     * Otherwise, the original string will be returned.
     *
     * @return a String representation of the StringNode
     */
    @Override
    public String toString() {
        return encodeStringByDefault ? source.toEncodedStringIfNeeded(start, end) : source.getString(start, end);
    }

    /**
     * Returns the encoded string representation of the StringNode.
     * The string is encoded using the source's encoding.
     *
     * @return the encoded string representation of the StringNode
     */
    public String toEncodedString() {
        return source.getEncodedString(start, end);
    }

    /**
     * Returns the unencoded string representation of the StringNode.
     * The original string is returned without encoding.
     *
     * @return the unencoded string representation of the StringNode
     */
    public String toUnencodedString() {
        return source.getString(start, end);
    }

    /**
     * Checks if the StringNode is equal to the specified object.
     * The comparison is performed by comparing the content as a CharSequence.
     *
     * @param o the object to compare with
     * @return {@code true} if the StringNode is equal to the object, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof CharSequence) {
            CharSequence other = (CharSequence) o;
            return CharSequenceUtils.equals(this, other);
        } else {
            return false;
        }
    }

    /**
     * Returns the hash code value for the StringNode.
     * The hash code is calculated based on the content as a CharSequence.
     *
     * @return the hash code value for the StringNode
     */
    @Override
    public int hashCode() {
        if (hashCodeSet) {
            return hashCode;
        }
        hashCode = CharSequenceUtils.hashCode(this);
        hashCodeSet = true;
        return hashCode;
    }
}
