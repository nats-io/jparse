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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The ScalarNode interface represents a scalar node in a tree structure.
 * <p>
 * Scalar nodes are leaf nodes that contain a single value.
 * <p>
 * Implementations of this interface should provide methods to access and manipulate the value of the scalar node.
 * <p>
 * The value can be of various types, including boolean, integer, long, double, BigDecimal, BigInteger,
 * CharSequence, and String.
 */
public interface ScalarNode extends Node {

    @Override
    default boolean isScalar() {
        return true;
    }

    @Override
    default boolean isCollection() {
        return false;
    }

    /**
     * Returns the value of the scalar node.
     *
     * @return the value of the scalar node
     */
    Object value();

    /**
     * Returns the boolean value of the scalar node.
     * This method should be implemented by concrete classes
     * that represent boolean scalar nodes.
     *
     * @return the boolean value of the scalar node
     * @throws UnsupportedOperationException if the method is not supported by the implementation
     */
    default boolean booleanValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the integer value of the scalar node.
     * This method should be implemented by concrete classes
     * that represent integer scalar nodes.
     *
     * @return the integer value of the scalar node
     * @throws UnsupportedOperationException if the method is not supported by the implementation
     */
    default int intValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the long value of the scalar node.
     * This method should be implemented by concrete classes
     * that represent long scalar nodes.
     *
     * @return the long value of the scalar node
     * @throws UnsupportedOperationException if the method is not supported by the implementation
     */
    default long longValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the double value of the scalar node.
     * This method should be implemented by concrete classes
     * that represent double scalar nodes.
     *
     * @return the double value of the scalar node
     * @throws UnsupportedOperationException if the method is not supported by the implementation
     */
    default double doubleValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the BigDecimal value of the scalar node.
     * This method should be implemented by concrete classes
     * that represent BigDecimal scalar nodes.
     *
     * @return the BigDecimal value of the scalar node
     * @throws UnsupportedOperationException if the method is not supported by the implementation
     */
    default BigDecimal bigDecimalValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the BigInteger value of the scalar node.
     * This method should be implemented by concrete classes
     * that represent BigInteger scalar nodes.
     *
     * @return the BigInteger value of the scalar node
     * @throws UnsupportedOperationException if the method is not supported by the implementation
     */
    default BigInteger bigIntegerValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the CharSequence value of the scalar node.
     * This method should be implemented by concrete classes
     * that represent CharSequence scalar nodes.
     *
     * @return the CharSequence value of the scalar node
     */
    default CharSequence charSequenceValue() {
        return this.originalCharSequence();
    }

    /**
     * Returns the String value of the scalar node.
     * This method should be implemented by concrete classes
     * that represent String scalar nodes.
     *
     * @return the String value of the scalar node
     */
    default String stringValue() {
        return this.originalString();
    }

    /**
     * Checks if the scalar node is equal to the specified string.
     *
     * @param str the string to compare with
     * @return {@code true} if the scalar node is equal to the specified string, {@code false} otherwise
     */
    default boolean equalsString(String str) {
        return this.equals(str);
    }

}
