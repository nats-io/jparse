package com.cloudurable.jparse.node;

import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;

import java.util.Collections;
import java.util.List;

public class NullNode implements ScalarNode {


    private final Token token;
    private final CharSource source;


    public NullNode(final Token token, final CharSource source) {
        this.token = token;
        this.source = source;
    }

    @Override
    public NodeType type() {
        return NodeType.NULL;
    }

    @Override
    public List<Token> tokens() {
        return Collections.singletonList(token);
    }

    @Override
    public Token rootElementToken() {
        return token;
    }

    @Override
    public CharSource charSource() {
        return source;
    }

    @Override
    public int length() {
        return 4;
    }

    @Override
    public char charAt(int index) {
        return switch (index) {
            case 0 -> 'n';
            case 1 -> 'u';
            case 2 -> 'l';
            case 3 -> 'l';
            default -> throw new IllegalStateException();
        };
    }

    @Override
    public Object value() {
        return null;
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

}
