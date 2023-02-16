package com.cloudurable.jparse.path;

import com.cloudurable.jparse.node.NodeType;
import com.cloudurable.jparse.node.ScalarNode;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;

import java.util.Collections;
import java.util.List;

public class KeyPathNode implements ScalarNode, PathElement {

    private final Token rootElementToken;
    private final CharSource charSource;

    public KeyPathNode(final Token token, final CharSource charSource) {
        this.rootElementToken = token;
        this.charSource = charSource;
    }

    @Override
    public NodeType type() {
        return NodeType.PATH_KEY;
    }

    @Override
    public List<Token> tokens() {
        return Collections.singletonList(rootElementToken);
    }

    @Override
    public Token rootElementToken() {
        return rootElementToken;
    }

    @Override
    public CharSource charSource() {
        return charSource;
    }


    @Override
    public Object value() {
        return toString();
    }

    @Override
    public boolean isIndex() {
        return false;
    }

    @Override
    public boolean isKey() {
        return true;
    }

    @Override
    public String toString() {
        return this.originalString();
    }

    public CharSequence toCharSequence() {
        return this.originalCharSequence();
    }
}
