package com.cloudurable.jparse.node;

import com.cloudurable.jparse.node.support.CharSequenceUtils;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.token.Token;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class NumberNode extends Number implements ScalarNode, CharSequence {

    private final Token token;
    private final CharSource source;
    private final NodeType elementType;
    private boolean hashCodeSet;
    private int hashCode;

    public NumberNode(Token token, CharSource source, NodeType elementType) {
        this.token = token;
        this.source = source;
        this.elementType = elementType;
    }

    @Override
    public int intValue() {
        return source.getInt(token.startIndex(), token.endIndex());
    }

    @Override
    public long longValue() {
        return source.getLong(token.startIndex(), token.endIndex());
    }

    @Override
    public float floatValue() {
        return source.getFloat(token.startIndex(), token.endIndex());
    }

    @Override
    public double doubleValue() {
        return source.getDouble(token.startIndex(), token.endIndex());
    }


    @Override
    public BigDecimal bigDecimalValue() {
        return source.getBigDecimal(token.startIndex(), token.endIndex());
    }

    @Override
    public BigInteger bigIntegerValue() {
        return source.getBigInteger(token.startIndex(), token.endIndex());
    }


    @Override
    public Object value() {
        if (isInteger()) {
            return intValue();
        } else if (isLong()) {
            return longValue();
        } else {
            return this.doubleValue();
        }
    }

    @Override
    public NodeType type() {
        return this.elementType;
    }

    @Override
    public int length() {
        return token.endIndex() - token.startIndex();
    }

    @Override
    public char charAt(int index) {
        if (index > length()) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return source.getChartAt(token.startIndex() + index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (end > length()) {
            throw new IndexOutOfBoundsException();
        }
        return source.getCharSequence(start + token.startIndex(), end + token.startIndex());
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
        if (o == null || getClass() != o.getClass()) return false;
        NumberNode other = (NumberNode) o;
        return CharSequenceUtils.equals(this, other);
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

    public boolean isInteger() {
        return switch (elementType) {
            case INT -> source.isInteger(this.token.startIndex(), this.token.endIndex());
            default -> false;
        };
    }

    public boolean isLong() {
        return switch (elementType) {
            case INT -> !source.isInteger(this.token.startIndex(), this.token.endIndex());
            default -> false;
        };
    }


    @Override
    public String toString() {
        return this.originalString();
    }
}
