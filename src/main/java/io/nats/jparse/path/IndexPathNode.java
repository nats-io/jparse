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
package io.nats.jparse.path;

import io.nats.jparse.node.NodeType;
import io.nats.jparse.node.ScalarNode;
import io.nats.jparse.node.support.CharSequenceUtils;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.token.Token;

import java.util.Collections;
import java.util.List;


/**
 * Represents a index element of a JSON Path expression.
 * <p>
 * Represents an index path node in the parse tree which also implements
 * the ScalarNode, CharSequence, and PathElement interfaces.
 */
public class IndexPathNode extends Number implements ScalarNode, CharSequence, PathElement {

    /**
     * The token associated with this path node.
     */
    private final Token token;

    /**
     * The source of characters where this path node comes from.
     */
    private final CharSource source;

    /**
     * A flag indicating whether the hashCode has been set for this path node.
     */
    private boolean hashCodeSet;

    /**
     * The hash code of this path node. This is computed and cached for performance.
     */
    private int hashCode;

    /**
     * Constructor for IndexPathNode.
     *
     * @param token  The token representing the node.
     * @param source The source of characters to be parsed.
     */
    public IndexPathNode(Token token, CharSource source) {
        this.token = token;
        this.source = source;
    }

    // Overridden Number class methods with their appropriate Javadoc comments

    /**
     * Returns the value of this node as an int.
     *
     * @return The int value represented by this object.
     */
    @Override
    public int intValue() {
        return source.getInt(token.startIndex, token.endIndex);
    }

    /**
     * Returns the value of this node as a long.
     *
     * @return The long value represented by this object.
     */
    @Override
    public long longValue() {
        return intValue();
    }

    /**
     * Returns the value of this node as a float.
     *
     * @return The float value represented by this object.
     */
    @Override
    public float floatValue() {
        return intValue();
    }

    /**
     * Returns the value of this node as a double.
     *
     * @return The double value represented by this object.
     */
    @Override
    public double doubleValue() {
        return intValue();
    }

    /**
     * Returns the type of the node.
     *
     * @return The type of the node.
     */
    @Override
    public NodeType type() {
        return NodeType.PATH_INDEX;
    }

    /**
     * Returns the value of the node.
     *
     * @return The value of the node.
     */
    @Override
    public Object value() {
        return intValue();
    }

    /**
     * Returns the list of tokens representing the node.
     *
     * @return The list of tokens representing the node.
     */
    @Override
    public List<Token> tokens() {
        return Collections.singletonList(this.token);
    }

    /**
     * Returns the root element token.
     *
     * @return The root element token.
     */
    @Override
    public Token rootElementToken() {
        return token;
    }

    /**
     * Returns the char source of the node.
     *
     * @return The char source of the node.
     */
    @Override
    public CharSource charSource() {
        return source;
    }

    /**
     * Indicates whether some other object is equal to this one.
     *
     * @param o The reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
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
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
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
     * Indicates whether the node is an index.
     *
     * @return true if the node is an index; false otherwise.
     */
    @Override
    public boolean isIndex() {
        return true;
    }

    /**
     * Indicates whether the node is a key.
     *
     * @return true if the node is a key; false otherwise.
     */
    @Override
    public boolean isKey() {
        return false;
    }
}
