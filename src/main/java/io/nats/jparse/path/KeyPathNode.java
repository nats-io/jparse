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
import io.nats.jparse.source.CharSource;
import io.nats.jparse.token.Token;

import java.util.Collections;
import java.util.List;

/**
 * Represents a key element of a JSON Path expression.
 * <p>
 * Contains methods for determining the type of element and for converting it to a key representation.
 */
public class KeyPathNode implements ScalarNode, PathElement {

    private final Token rootElementToken;
    private final CharSource charSource;


    /**
     * Construct KeyPathNode.
     * @param token token
     * @param charSource charSource
     */
    public KeyPathNode(final Token token, final CharSource charSource) {
        this.rootElementToken = token;
        this.charSource = charSource;
    }

    /**
     * Returns the type of this node.
     *
     * @return a `NodeType` representing this node's type
     */
    @Override
    public NodeType type() {
        return NodeType.PATH_KEY;
    }

    /**
     * Returns a list of tokens representing this node.
     *
     * @return a list of `Token`s representing this node
     */
    @Override
    public List<Token> tokens() {
        return Collections.singletonList(rootElementToken);
    }

    /**
     * Returns the root token of this node.
     *
     * @return the root `Token` of this node
     */
    @Override
    public Token rootElementToken() {
        return rootElementToken;
    }

    /**
     * Returns the character source of this node.
     *
     * @return the `CharSource` of this node
     */
    @Override
    public CharSource charSource() {
        return charSource;
    }

    /**
     * Returns the value of this node.
     *
     * @return a string representation of this node
     */
    @Override
    public Object value() {
        return toString();
    }

    /**
     * Determines whether this element is an index.
     *
     * @return `false`, since a `KeyPathNode` represents a key, not an index
     */
    @Override
    public boolean isIndex() {
        return false;
    }

    /**
     * Determines whether this element is a key.
     *
     * @return `true`, since a `KeyPathNode` represents a key
     */
    @Override
    public boolean isKey() {
        return true;
    }

    /**
     * Returns a string representation of this node.
     *
     * @return a string representation of this node
     */
    @Override
    public String toString() {
        return this.originalString();
    }

    /**
     * Returns a character sequence representation of this node.
     *
     * @return a character sequence representation of this node
     */
    public CharSequence toCharSequence() {
        return this.originalCharSequence();
    }
}

