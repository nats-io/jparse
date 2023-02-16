package com.cloudurable.jparse.path;

import com.cloudurable.jparse.node.ArrayNode;
import com.cloudurable.jparse.node.CollectionNode;
import com.cloudurable.jparse.node.Node;
import com.cloudurable.jparse.node.NodeType;
import com.cloudurable.jparse.node.support.NodeUtils;
import com.cloudurable.jparse.node.support.TokenSubList;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;

import java.util.*;
import java.util.stream.Collectors;

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

        for (int index = 0; index < this.tokens.size(); index++) {
            var thisValue = this.tokens.get(index);
            var otherValue = other.tokens.get(index);
            if (otherValue == null && thisValue == null) continue;
            var thisStr = thisValue.asString(this.source);
            var otherStr = otherValue.asString(other.source);
            if (!thisStr.equals(otherStr)) {
                return false;
            }
        }
        return true;
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

        return new Iterator<>() {
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
