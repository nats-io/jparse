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

import io.nats.jparse.node.CollectionNode;
import io.nats.jparse.node.Node;
import io.nats.jparse.node.NodeType;
import io.nats.jparse.node.support.NodeUtils;
import io.nats.jparse.node.support.TokenSubList;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.token.Token;

import java.util.*;
import java.util.stream.Collectors;
/**
 * PathNode class represents a node in the parsed path of a structured data source such as JSON or XML.
 * It extends AbstractList and implements the CollectionNode interface.
 */
public class PathNode extends AbstractList<PathElement> implements CollectionNode {

    /**
     * The list of tokens associated with this path node.
     */
    private final TokenSubList tokens;

    /**
     * The source of characters where this path node comes from.
     */
    private final CharSource source;

    /**
     * The root token of this path node.
     */
    private final Token rootToken;

    /**
     * The hash code of this path node. This is computed and cached for performance.
     */
    private int hashCode;

    /**
     * The list of child tokens of this path node.
     */
    private List<List<Token>> childrenTokens;

    /**
     * The array of elements (node structures) represented by this path node.
     */
    private Node[] elements;

    /**
     * A flag indicating whether the hashCode has been set for this path node.
     */
    private boolean hashCodeSet;

    /**
     * Constructor for the PathNode class.
     *
     * @param tokens The list of tokens to associate with this path node.
     * @param source The character source from which this path node originates.
     */
    public PathNode(final TokenSubList tokens, final CharSource source) {
        this.tokens = tokens;
        this.rootToken = tokens.get(0);
        this.source = source;
    }

    /**
     * Retrieves the list of child tokens associated with this path node.
     * If the list has not been initialized yet, it is created by streaming the token array
     * and collecting each token as a singleton list.
     *
     * @return the list of child token lists.
     */
    @Override
    public List<List<Token>> childrenTokens() {
        if (childrenTokens == null) {
            childrenTokens = Arrays.stream(tokens.toArray()).map(Collections::singletonList).collect(Collectors.toList());
        }
        return childrenTokens;
    }

    /**
     * Gets the array of elements (node structures) represented by this path node.
     * If the array has not been initialized yet, it is created with the size equal to the number of tokens.
     *
     * @return the array of elements.
     */
    Node[] elements() {
        if (elements == null) {
            elements = new Node[tokens.size()];
        }
        return elements;
    }

    /**
     * Retrieves the node associated with the specified key.
     *
     * @param key the key used to retrieve the node.
     * @return the node at the given key.
     */
    @Override
    public Node getNode(Object key) {
        return this.getNodeAt(Integer.valueOf((String) key));
    }

    /**
     * Retrieves the node at the specified index.
     *
     * @param index the index of the node to retrieve.
     * @return the node at the given index.
     */
    public Node getNodeAt(int index) {
        Node element = elements()[index];
        if (element == null) {
            List<Token> tokens = Collections.singletonList(this.tokens.get(index));
            elements()[index] = NodeUtils.createNode(tokens, source, false);
        }
        return elements()[index];
    }

    /**
     * Retrieves the path index value at the specified index.
     *
     * @param index the index of the path to retrieve.
     * @return the path index value at the given index.
     */
    public int getPathIndexValue(int index) {
        return getIndexNode(index).intValue();
    }

    /**
     * Retrieves the path key value at the specified index.
     *
     * @param index the index of the path to retrieve.
     * @return the path key value at the given index.
     */
    public String getPathKeyValue(int index) {
        return getKeyNode(index).toString();
    }

    /**
     * Retrieves the key node at the specified index.
     *
     * @param index the index of the key node to retrieve.
     * @return the key node at the given index.
     */
    public KeyPathNode getKeyNode(int index) {
        return (KeyPathNode) getNodeAt(index);
    }

    /**
     * Retrieves the index node at the specified index.
     *
     * @param index the index of the index node to retrieve.
     * @return the index node at the given index.
     */
    public IndexPathNode getIndexNode(int index) {
        return (IndexPathNode) getNodeAt(index);
    }

    /**
     * Retrieves the number of elements (node structures) represented by this path node.
     *
     * @return the number of elements.
     */
    public int length() {
        return elements().length;
    }

    /**
     * Returns the type of this node, which is NodeType.PATH.
     *
     * @return the NodeType.PATH enum.
     */
    @Override
    public NodeType type() {
        return NodeType.PATH;
    }

    /**
     * Retrieves the list of tokens associated with this path node.
     *
     * @return the list of tokens.
     */
    @Override
    public List<Token> tokens() {
        return tokens;
    }

    /**
     * Retrieves the root token of this path node.
     *
     * @return the root token.
     */
    @Override
    public Token rootElementToken() {
        return rootToken;
    }

    /**
     * Retrieves the character source associated with this path node.
     *
     * @return the character source.
     */
    @Override
    public CharSource charSource() {
        return source;
    }


    /**
     * Retrieves the path element at the specified index.
     *
     * @param index the index of the path element to retrieve.
     * @return the path element at the given index.
     */
    @Override
    public PathElement get(int index) {
        return (PathElement) getNodeAt(index);
    }

    /**
     * Checks if the specified object is equal to this path node.
     * Equality is based on the size and string representation of the tokens.
     *
     * @param o the object to compare with this path node.
     * @return true if the specified object is equal to this path node, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PathNode)) return false;

        final PathNode other = (PathNode) o;

        if (this.tokens.size() != other.tokens.size()) {
            return false;
        }

        for (int index = 0; index < this.tokens.size(); index++) {
            Token thisValue = this.tokens.get(index);
            Token otherValue = other.tokens.get(index);
            if (otherValue == null && thisValue == null) continue;
            String thisStr = thisValue.asString(this.source);
            String otherStr = otherValue.asString(other.source);
            if (!thisStr.equals(otherStr)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Computes and retrieves the hash code of this path node.
     * The hash code is computed based on the string representation of the tokens.
     * Once computed, it is stored for subsequent calls.
     *
     * @return the hash code of this path node.
     */
    @Override
    public int hashCode() {
        if (hashCodeSet) {
            return hashCode;
        }
        hashCode = Objects.hash(tokens.stream().map(tok -> tok.asString(this.source)).collect(Collectors.toList()));
        hashCodeSet = true;
        return hashCode;
    }


    /**
     * Retrieves the size of this path node, represented by the number of tokens.
     *
     * @return the size of this path node.
     */
    @Override
    public int size() {
        return tokens().size();
    }

    /**
     * Provides an iterator over the path elements of this path node.
     *
     * @return an iterator over the path elements.
     */
    @Override
    public Iterator<PathElement> iterator() {

        return new Iterator<PathElement>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < tokens().size();
            }

            @Override
            public PathElement next() {
                return (PathElement) getNodeAt(index++);
            }
        };
    }
}
