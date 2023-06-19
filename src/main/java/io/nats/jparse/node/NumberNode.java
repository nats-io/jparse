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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

/**
 * The NumberNode class represents a numeric node in a tree structure.
 * <p>
 * It implements the ScalarNode and CharSequence interfaces.
 * <p>
 * Number nodes can store integer, long, float, and double values.
 */
public class NumberNode extends Number implements ScalarNode, CharSequence {

    /**
     * Token associated with this number node.
     * This token represents a parsed number from the source character sequence.
     */
    private final Token token;

    /**
     * Source of characters used to create the token.
     * This source is used to perform further operations like extracting string or sub-sequence.
     */
    private final CharSource source;

    /**
     * The type of this node.
     * This type defines the type of the value this node represents (integer, float, etc.).
     */
    private final NodeType elementType;

    /**
     * Flag to indicate whether the hash code has been computed or not.
     * It helps to avoid recomputing the hash code for every call of hashCode() method.
     */
    private boolean hashCodeSet;

    /**
     * Cache for the hash code of this object.
     * Once computed, the hash code is stored in this variable for subsequent calls.
     */
    private int hashCode;


    /**
     * Constructs a NumberNode with the specified token, source, and element type.
     *
     * @param token       the token representing the numeric value
     * @param source      the character source containing the numeric value
     * @param elementType the element type of the number node
     */
    public NumberNode(Token token, CharSource source, NodeType elementType) {
        this.token = token;
        this.source = source;
        this.elementType = elementType;
    }

    /**
     * Returns the integer value of the number node.
     *
     * @return the integer value of the number node
     */
    @Override
    public int intValue() {
        return source.getInt(token.startIndex, token.endIndex);
    }

    /**
     * Returns the long value of the number node.
     *
     * @return the long value of the number node
     */
    @Override
    public long longValue() {
        return source.getLong(token.startIndex, token.endIndex);
    }

    /**
     * Returns the float value of the number node.
     *
     * @return the float value of the number node
     */
    @Override
    public float floatValue() {
        return source.getFloat(token.startIndex, token.endIndex);
    }

    /**
     * Returns the double value of the number node.
     *
     * @return the double value of the number node
     */
    @Override
    public double doubleValue() {
        return source.getDouble(token.startIndex, token.endIndex);
    }

    /**
     * Returns the BigDecimal value of the number node.
     *
     * @return the BigDecimal value of the number node
     */
    public BigDecimal bigDecimalValue() {
        return source.getBigDecimal(token.startIndex, token.endIndex);
    }

    /**
     * Returns the BigInteger value of the number node.
     *
     * @return the BigInteger value of the number node
     */
    public BigInteger bigIntegerValue() {
        return source.getBigInteger(token.startIndex, token.endIndex);
    }

    /**
     * Returns the value of the number node as an Object.
     * If the number node represents an integer, an Integer object is returned.
     * If the number node represents a long, a Long object is returned.
     * Otherwise, a Double object is returned.
     *
     * @return the value of the number node as an Object
     */
    @Override
    public Object value() {
        if (isInteger()) {
            return intValue();
        } else if (isLong()) {
            return longValue();
        } else {
            return this.doubleValue();
        }
    }

    /**
     * Returns the element type of the number node.
     *
     * @return the element type of the number node
     */
    @Override
    public NodeType type() {
        return this.elementType;
    }

    /**
     * Returns the length of the number node.
     *
     * @return the length of the number node
     */
    @Override
    public int length() {
        return token.endIndex - token.startIndex;
    }

    /**
     * Returns the character at the specified index in the number node.
     *
     * @param index the index of the character to retrieve
     * @return the character at the specified index
     * @throws ArrayIndexOutOfBoundsException if the index is out of range
     */
    @Override
    public char charAt(int index) {
        if (index > length()) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return source.getChartAt(token.startIndex + index);
    }

    /**
     * Returns a CharSequence that is a subsequence of the number node.
     *
     * @param start the start index of the subsequence (inclusive)
     * @param end   the end index of the subsequence (exclusive)
     * @return the subsequence of the number node
     * @throws IndexOutOfBoundsException if the start or end index is out of range
     */
    @Override
    public CharSequence subSequence(int start, int end) {
        if (end > length()) {
            throw new IndexOutOfBoundsException();
        }
        return source.getCharSequence(start + token.startIndex, end + token.startIndex);
    }

    /**
     * Returns a list containing the token of the number node.
     *
     * @return a list containing the token of the number node
     */
    @Override
    public List<Token> tokens() {
        return Collections.singletonList(this.token);
    }

    /**
     * Returns the root element token of the number node.
     *
     * @return the root element token of the number node
     */
    @Override
    public Token rootElementToken() {
        return token;
    }

    /**
     * Returns the character source associated with the number node.
     *
     * @return the character source associated with the number node
     */
    @Override
    public CharSource charSource() {
        return source;
    }

    /**
     * Checks if the number node is equal to the specified object.
     * <p>
     * The number node is considered equal to another object if:
     * <p>
     * The other object is an instance of NumberNode and has the same CharSequence representation.
     * The other object is an instance of Number and has the same numeric value.
     *
     * @param o the object to compare with the number node
     * @return true if the number node is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (o instanceof NumberNode) {
            NumberNode other = (NumberNode) o;
            return CharSequenceUtils.equals(this, other);
        } else if (o instanceof Number) {
            switch (o.getClass().getName()) {
                case "java.lang.Integer":
                    return this.intValue() == (int) o;
                case "java.lang.Long":
                    return this.longValue() == (long) o;
                case "java.lang.Float":
                    return this.floatValue() == (float) o;
                case "java.lang.Double":
                    return this.doubleValue() == (double) o;
                case "java.math.BigDecimal":
                    return this.bigDecimalValue().equals(o);
                case "java.math.BigInteger":
                    return this.bigIntegerValue().equals(o);
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Returns the hash code value for the number node.
     *
     * @return the hash code value for the number node
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

    /**
     * Checks if the number node represents an integer value.
     *
     * @return true if the number node represents an integer value, false otherwise
     */
    public boolean isInteger() {
        switch (elementType) {
            case INT:
                return source.isInteger(this.token.startIndex, this.token.endIndex);
            default:
                return false;
        }
    }

    /**
     * Checks if the number node represents a long value.
     *
     * @return true if the number node represents a long value, false otherwise
     */
    public boolean isLong() {
        switch (elementType) {
            case INT:
                return !source.isInteger(this.token.startIndex, this.token.endIndex);
            default:
                return false;
        }
    }

    /**
     * Returns the string representation of the number node.
     *
     * @return the string representation of the number node
     */
    @Override
    public String toString() {
        return this.originalString();
    }
}
