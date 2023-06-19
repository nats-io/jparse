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
 * The BooleanNode class represents a boolean value node in a tree structure.
 * <p>
 * It implements the ScalarNode interface.
 */
public class BooleanNode implements ScalarNode {

    private final Token token;
    private final CharSource source;
    private final boolean value;

    /**
     * Constructs a BooleanNode with the specified token and source.
     *
     * @param token  the token representing the boolean value
     * @param source the character source containing the boolean value
     */
    public BooleanNode(final Token token, final CharSource source) {
        this.token = token;
        this.source = source;
        this.value = source.getChartAt(token.startIndex) == 't';
    }

    /**
     * Returns the element type of the boolean node.
     *
     * @return the element type of the boolean node
     */
    @Override
    public NodeType type() {
        return NodeType.BOOLEAN;
    }

    /**
     * Returns a list containing the token of the boolean node.
     *
     * @return a list containing the token of the boolean node
     */
    @Override
    public List<Token> tokens() {
        return Collections.singletonList(token);
    }

    /**
     * Returns the root element token of the boolean node.
     *
     * @return the root element token of the boolean node
     */
    @Override
    public Token rootElementToken() {
        return token;
    }

    /**
     * Returns the character source associated with the boolean node.
     *
     * @return the character source associated with the boolean node
     */
    @Override
    public CharSource charSource() {
        return source;
    }

    /**
     * Returns the boolean value of the boolean node.
     *
     * @return the boolean value of the boolean node
     */
    public boolean booleanValue() {
        return value;
    }

    /**
     * Returns the length of the boolean node.
     *
     * @return the length of the boolean node
     */
    @Override
    public int length() {
        return value ? 4 : 5;
    }

    /**
     * Returns the value of the boolean node.
     *
     * @return the value of the boolean node
     */
    @Override
    public Object value() {
        return booleanValue();
    }

    /**
     * Returns the character at the specified index in the boolean node.
     *
     * @param index the index of the character to retrieve
     * @return the character at the specified index
     * @throws IllegalStateException if the index is out of range
     */
    @Override
    public char charAt(int index) {
        if (value) {
            switch (index) {
                case 0:
                    return 't';
                case 1:
                    return 'r';
                case 2:
                    return 'u';
                case 3:
                    return 'e';
                default:
                    throw new IllegalStateException();
            }
        } else {
            switch (index) {
                case 0:
                    return 'f';
                case 1:
                    return 'a';
                case 2:
                    return 'l';
                case 3:
                    return 's';
                case 4:
                    return 'e';
                default:
                    throw new IllegalStateException();
            }
        }
    }

    /**
     * Returns the string representation of the boolean node.
     *
     * @return the string representation of the boolean node
     */
    @Override
    public String toString() {
        return value ? "true" : "false";
    }

    /**
     * Checks if the boolean node is equal to the specified object.
     * <p>
     * The boolean node is considered equal to another object if the other object is also a BooleanNode
     * <p>
     * and has the same boolean value.
     *
     * @param o the object to compare with the boolean node
     * @return true if the boolean node is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof BooleanNode) {
            BooleanNode that = (BooleanNode) o;
            return value == that.value;
        } else if (o instanceof Boolean) {
            Boolean that = (Boolean) o;
            return value == that;
        }
        return false;
    }

    /**
     * Returns the hash code value for the boolean node.
     *
     * @return the hash code value for the boolean node
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
