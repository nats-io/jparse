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

import io.nats.jparse.node.ArrayNode;
import io.nats.jparse.node.CollectionNode;
import io.nats.jparse.node.Node;
import io.nats.jparse.node.NodeType;
import io.nats.jparse.node.support.NodeUtils;
import io.nats.jparse.node.support.TokenSubList;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.token.Token;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PathNode extends AbstractList<PathElement> implements CollectionNode {


    private final TokenSubList tokens;
    private final CharSource source;
    private final Token rootToken;
    private int hashCode;
    private List<List<Token>> childrenTokens;
    private Node[] elements;
    private boolean hashCodeSet;

    public PathNode(final TokenSubList tokens, final CharSource source) {
        this.tokens = tokens;
        this.rootToken = tokens.get(0);
        this.source = source;
    }


    @Override
    public List<List<Token>> childrenTokens() {
        if (childrenTokens == null) {
            childrenTokens = NodeUtils.getChildrenTokens(tokens);
        }
        return childrenTokens;
    }


    Node[] elements() {
        if (elements == null) {
            elements = new Node[tokens.size()];
        }
        return elements;
    }

    @Override
    public Node getNode(Object key) {
        return this.getNodeAt(Integer.valueOf((String) key));
    }

    public Node getNodeAt(int index) {
        Node element = elements()[index];
        if (element == null) {
            List<Token> tokens = Collections.singletonList(this.tokens.get(index));
            elements()[index] = NodeUtils.createNode(tokens, source, false);
        }
        return elements()[index];
    }

    public int getPathIndexValue(int index) {
        return getIndexNode(index).intValue();
    }

    public String getPathKeyValue(int index) {
        return getKeyNode(index).toString();
    }

    public KeyPathNode getKeyNode(int index) {
        return (KeyPathNode) getNodeAt(index);
    }

    public IndexPathNode getIndexNode(int index) {
        return (IndexPathNode) getNodeAt(index);
    }

    public int length() {
        return elements().length;
    }

    @Override
    public NodeType type() {
        return NodeType.ARRAY;
    }

    @Override
    public List<Token> tokens() {
        return tokens;
    }

    @Override
    public Token rootElementToken() {
        return rootToken;
    }

    @Override
    public CharSource charSource() {
        return source;
    }


    @Override
    public PathElement get(int index) {
        return (PathElement) getNodeAt(index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrayNode)) return false;

        final PathNode other = (PathNode) o;

        if (this.tokens.size() != other.tokens.size()) {
            return false;
        }

        return IntStream.range(0, this.tokens.size())
                .allMatch(index -> {
                    Token thisValue = this.tokens.get(index);
                    Token otherValue = other.tokens.get(index);
                    return (otherValue == null && thisValue == null) ||
                            thisValue.asString(this.source).equals(otherValue.asString(other.source));
                });
    }


    @Override
    public int hashCode() {
        if (hashCodeSet) {
            return hashCode;
        }
        hashCode = Objects.hash(tokens.stream().map(tok -> tok.asString(this.source)).collect(Collectors.toList()));
        hashCodeSet = true;
        return hashCode;
    }


    @Override
    public int size() {
        return tokens().size();
    }


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
