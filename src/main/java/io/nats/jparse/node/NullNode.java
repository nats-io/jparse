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

import io.nats.jparse.source.CharSource;
import io.nats.jparse.token.Token;

import java.util.Collections;
import java.util.List;

/**
 * The NullNode class represents a null value node in a tree structure.
 * <p>
 * It implements the ScalarNode interface.
 */
public class NullNode implements ScalarNode {

    private final Token token;
    private final CharSource source;

    /**
     * Constructs a NullNode with the specified token and source.
     *
     * @param token  the token representing the null value
     * @param source the character source containing the null value
     */
    public NullNode(final Token token, final CharSource source) {
        this.token = token;
        this.source = source;
    }

    /**
     * Returns the element type of the null node.
     *
     * @return the element type of the null node
     */
    @Override
    public NodeType type() {
        return NodeType.NULL;
    }

    /**
     * Returns a list containing the token of the null node.
     *
     * @return a list containing the token of the null node
     */
    @Override
    public List<Token> tokens() {
        return Collections.singletonList(token);
    }

    /**
     * Returns the root element token of the null node.
     *
     * @return the root element token of the null node
     */
    @Override
    public Token rootElementToken() {
        return token;
    }

    /**
     * Returns the character source associated with the null node.
     *
     * @return the character source associated with the null node
     */
    @Override
    public CharSource charSource() {
        return source;
    }

    /**
     * Returns the length of the null node.
     *
     * @return the length of the null node
     */
    @Override
    public int length() {
        return 4;
    }

    /**
     * Returns the character at the specified index in the null node.
     *
     * @param index the index of the character to retrieve
     * @return the character at the specified index
     * @throws IllegalStateException if the index is out of range
     */
    @Override
    public char charAt(int index) {
        switch (index) {
            case 0:
                return 'n';
            case 1:
                return 'u';
            case 2:
            case 3:
                return 'l';
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Returns the value of the null node, which is always null.
     *
     * @return the value of the null node
     */
    @Override
    public Object value() {
        return null;
    }

    /**
     * Returns the string representation of the null node.
     *
     * @return the string representation of the null node
     */
    @Override
    public String toString() {
        return "null";
    }

    /**
     * Checks if the null node is equal to the specified object.
     * The null node is considered equal to another object if the other object is also a NullNode.
     *
     * @param o the object to compare with the null node
     * @return true if the null node is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }

    /**
     * Returns the hash code value for the null node.
     *
     * @return the hash code value for the null node
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
