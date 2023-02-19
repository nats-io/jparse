package com.cloudurable.jparse.path;

import com.cloudurable.jparse.node.NodeType;
import com.cloudurable.jparse.node.ScalarNode;
import com.cloudurable.jparse.node.support.CharSequenceUtils;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;

import java.util.Collections;
import java.util.List;

public class IndexPathNode extends Number implements ScalarNode, CharSequence, PathElement {

    private final Token token;
    private final CharSource source;

    private boolean hashCodeSet;
    private int hashCode;

    public IndexPathNode(Token token, CharSource source) {
        this.token = token;
        this.source = source;
    }

    @Override
    public int intValue() {
        return source.getInt(token.startIndex, token.endIndex);
    }

    @Override
    public long longValue() {
        return intValue();
    }

    @Override
    public float floatValue() {
        return intValue();
    }

    @Override
    public double doubleValue() {
        return intValue();
    }

    @Override
    public NodeType type() {
        return NodeType.PATH_INDEX;
    }

    @Override
    public Object value() {
        return intValue();
    }

    @Override
    public List<Token> tokens() {
        return Collections.singletonList(this.token);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof CharSequence) {
            CharSequence other = (CharSequence) o;
            return CharSequenceUtils.equals(this, other);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (hashCodeSet) {
            return hashCode;
        }
        hashCode = CharSequenceUtils.hashCode(this);
        hashCodeSet = true;
        return hashCode;
    }


    @Override
    public boolean isIndex() {
        return true;
    }

    @Override
    public boolean isKey() {
        return false;
    }
}
